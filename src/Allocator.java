import java.util.Arrays;
import java.util.Stack;

public class Allocator {
    private final IRNode tail;
    private final IRNode head;
    private int VRName;
    private int MAXLIVE;


    public Allocator(IRNode tail, IRNode head) {
        this.tail = tail;
        this.head = head;
        this.VRName = -1;
        this.MAXLIVE = 0;
    }

    public void rename(int maxSR) {

        int[] SRToVR = new int[maxSR + 1];
        int[] LastUsed = new int[maxSR + 1];
        int index = tail.getIndex();
        int currLive = 0;
        for (int i = 0; i < maxSR + 1; i++) {
            SRToVR[i] = -1;
            LastUsed[i] = -1;
        }

        IRNode currentNode = tail;
        while (currentNode != null) {
            switch (currentNode.getOpCategory()) {
                case 0 -> {
                    // load
                    if (currentNode.getOpCode() == 0) {
                        // def
                        if (SRToVR[currentNode.getSR(3)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(3)] = VRName;
                        }
                        currentNode.setOperands(SRToVR[currentNode.getSR(3)], 9);
                        currentNode.setOperands(LastUsed[currentNode.getSR(3)], 11);
                        SRToVR[currentNode.getSR(3)] = -1;
                        LastUsed[currentNode.getSR(3)] = -1;
                        currLive--;

                        // use
                        if (SRToVR[currentNode.getSR(1)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(1)] = VRName;
                            currLive++;
                        }
                        currentNode.setOperands(SRToVR[currentNode.getSR(1)], 1);
                        currentNode.setOperands(LastUsed[currentNode.getSR(1)], 3);

                        LastUsed[currentNode.getSR(1)] = index;
                    } else {
                        // store

                        //use
                        if (SRToVR[currentNode.getSR(1)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(1)] = VRName;
                            currLive++;
                        }
                        currentNode.setOperands(SRToVR[currentNode.getSR(1)], 1);
                        currentNode.setOperands(LastUsed[currentNode.getSR(1)], 3);

                        //use
                        if (SRToVR[currentNode.getSR(3)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(3)] = VRName;
                            currLive++;
                        }
                        currentNode.setOperands(SRToVR[currentNode.getSR(3)], 9);
                        currentNode.setOperands(LastUsed[currentNode.getSR(3)], 11);

                        LastUsed[currentNode.getSR(1)] = index;
                        LastUsed[currentNode.getSR(3)] = index;
                    }
                }
                case 1 -> {
                    // loadI

                    // def
                    if (SRToVR[currentNode.getSR(3)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(3)] = VRName;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(3)], 9);
                    currentNode.setOperands(LastUsed[currentNode.getSR(3)], 11);
                    SRToVR[currentNode.getSR(3)] = -1;
                    LastUsed[currentNode.getSR(3)] = -1;
                    currLive--;
                }
                case 2 -> {
                    // arith
                    // def
                    if (SRToVR[currentNode.getSR(3)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(3)] = VRName;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(3)], 9);
                    currentNode.setOperands(LastUsed[currentNode.getSR(3)], 11);
                    SRToVR[currentNode.getSR(3)] = -1;
                    LastUsed[currentNode.getSR(3)] = -1;
                    currLive--;


                    // use
                    if (SRToVR[currentNode.getSR(1)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(1)] = VRName;
                        currLive++;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(1)], 1);
                    currentNode.setOperands(LastUsed[currentNode.getSR(1)], 3);

                    // use
                    if (SRToVR[currentNode.getSR(2)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(2)] = VRName;
                        currLive++;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(2)], 5);
                    currentNode.setOperands(LastUsed[currentNode.getSR(2)], 7);

                    LastUsed[currentNode.getSR(1)] = index;
                    LastUsed[currentNode.getSR(2)] = index;
                }
            }
            currentNode = currentNode.getPrev();
            index--;
            if (currLive > MAXLIVE) {
                MAXLIVE = currLive;
            }
        }
    }

    public void allocate(int numRegisters) {
        int[] VRToPR = new int[this.VRName + 1];
        int[] VRToSpillLocation = new int[this.VRName + 1];
        int[] PRToVR;
        int[] PRNU;
        int currPR = -1;
        int currSpillLoc = 32768;
        int currLastPRNU;
        Stack<Integer> PRStack = new Stack<>();

        if (numRegisters < MAXLIVE) {
            PRToVR = new int[numRegisters - 1];
            PRNU = new int[numRegisters - 1];
            for(int i = numRegisters - 2; i >= 0; i--) {
                PRToVR[i] = -1;
                PRStack.push(i);
                PRNU[i] = -1;
            }
        } else {
            PRToVR = new int[numRegisters];
            PRNU = new int[numRegisters];
           for( int i = numRegisters - 1; i >= 0; i--) {
               PRToVR[i] = -1;
               PRStack.push(i);
               PRNU[i] = -1;
           }
        }
        Arrays.fill(VRToPR, -1);

        IRNode currentNode = this.head;
        while (currentNode != null) {
            switch (currentNode.getOpCategory()) {
                case 0 -> {
                    // load
                    if (currentNode.getOpCode() == 0) {
                        // use
                        if (VRToPR[currentNode.getVR(1)] != -1) {
                            currentNode.setOperands(VRToPR[currentNode.getVR(1)], 2);
                        } else {
                            if (!PRStack.empty()) {
                                currPR = PRStack.pop();
                            } else {
                                currLastPRNU = pickLastNU(PRNU);
                                VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                                VRToPR[PRToVR[currLastPRNU]] = -1;

                                IRNode tempLoadI = new IRNode();
                                tempLoadI.setOpType(1, 0);
                                tempLoadI.setOperands(currSpillLoc, 0);
                                tempLoadI.setOperands(numRegisters - 1, 10);
                                tempLoadI.setOperands(currentNode.getIndex(), 11);


                                IRNode tempStore = new IRNode();
                                tempStore.setOpType(0, 1);
                                tempStore.setOperands(PRToVR[currLastPRNU], 1);
                                tempStore.setOperands(currLastPRNU, 2);
                                tempStore.setOperands(numRegisters - 1, 10);

                                currentNode.getPrev().setNext(tempLoadI);
                                tempLoadI.setPrev(currentNode.getPrev());
                                tempLoadI.setNext(tempStore);
                                tempStore.setPrev(tempLoadI);
                                tempStore.setNext(currentNode);
                                currentNode.setPrev(tempStore);
                                currSpillLoc += 4;
                                currPR = currLastPRNU;
                            }

                            PRToVR[currPR] = currentNode.getVR(1);
                            VRToPR[currentNode.getVR(1)] = currPR;
                            PRNU[currPR] = currentNode.getNU(1);
                            currentNode.setOperands(currPR, 2);

                            IRNode tempLoadI = new IRNode();
                            IRNode tempLoad = new IRNode();

                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(VRToSpillLocation[currentNode.getVR(1)], 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);

                            tempLoad.setOpType(0, 0);
                            tempLoad.setOperands(numRegisters - 1, 2);
                            tempLoad.setOperands(currentNode.getVR(1), 9);

                            tempLoad.setOperands(currPR, 10);
                            tempLoad.setOperands(currentNode.getIndex(), 11);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempLoad);
                            tempLoad.setPrev(tempLoadI);
                            tempLoad.setNext(currentNode);
                            currentNode.setPrev(tempLoad);
                        }

                        if (currentNode.getNU(1) == -1 && !(PRToVR[currentNode.getPR(1)] == -1)) {
                            VRToPR[PRToVR[currentNode.getPR(1)]] = -1;
                            PRToVR[currentNode.getPR(1)] = -1;
                            PRNU[currentNode.getPR(1)] = -1;
                            PRStack.push(currentNode.getPR(1));
                        }

                        // def
                        if (!PRStack.empty()) {
                            currPR = PRStack.pop();
                        } else {
                            currLastPRNU = pickLastNU(PRNU);
                            VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                            VRToPR[PRToVR[currLastPRNU]] = -1;

                            IRNode tempLoadI = new IRNode();
                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(currSpillLoc, 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);


                            IRNode tempStore = new IRNode();
                            tempStore.setOpType(0, 1);
                            tempStore.setOperands(PRToVR[currLastPRNU], 1);
                            tempStore.setOperands(currLastPRNU, 2);
                            tempStore.setOperands(numRegisters - 1, 10);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempStore);
                            tempStore.setPrev(tempLoadI);
                            tempStore.setNext(currentNode);
                            currentNode.setPrev(tempStore);
                            currSpillLoc += 4;
                            currPR = currLastPRNU;

                        }
                        PRToVR[currPR] = currentNode.getVR(3);
                        VRToPR[currentNode.getVR(3)] = currPR;
                        PRNU[currPR] = currentNode.getNU(3);
                        currentNode.setOperands(currPR, 10);

                    } else {
                        // store

                        // use
                        if (VRToPR[currentNode.getVR(1)] != -1) {
                            currentNode.setOperands(VRToPR[currentNode.getVR(1)], 2);
                        } else {
                            if (!PRStack.empty()) {
                                currPR = PRStack.pop();
                            } else {
                                currLastPRNU = pickLastNU(PRNU);
                                VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                                VRToPR[PRToVR[currLastPRNU]] = -1;

                                IRNode tempLoadI = new IRNode();
                                tempLoadI.setOpType(1, 0);
                                tempLoadI.setOperands(currSpillLoc, 0);
                                tempLoadI.setOperands(numRegisters - 1, 10);
                                tempLoadI.setOperands(currentNode.getIndex(), 11);


                                IRNode tempStore = new IRNode();
                                tempStore.setOpType(0, 1);
                                tempStore.setOperands(PRToVR[currLastPRNU], 1);
                                tempStore.setOperands(currLastPRNU, 2);
                                tempStore.setOperands(numRegisters - 1, 10);

                                currentNode.getPrev().setNext(tempLoadI);
                                tempLoadI.setPrev(currentNode.getPrev());
                                tempLoadI.setNext(tempStore);
                                tempStore.setPrev(tempLoadI);
                                tempStore.setNext(currentNode);
                                currentNode.setPrev(tempStore);
                                currSpillLoc += 4;
                                currPR = currLastPRNU;

                            }
                            PRToVR[currPR] = currentNode.getVR(1);
                            VRToPR[currentNode.getVR(1)] = currPR;
                            PRNU[currPR] = currentNode.getNU(1);
                            currentNode.setOperands(currPR, 2);

                            IRNode tempLoadI = new IRNode();
                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(VRToSpillLocation[currentNode.getVR(1)], 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);

                            IRNode tempLoad = new IRNode();
                            tempLoad.setOpType(0, 0);
                            tempLoad.setOperands(numRegisters - 1, 2);
                            tempLoad.setOperands(currentNode.getVR(1), 9);

                            tempLoad.setOperands(currPR, 10);
                            tempLoad.setOperands(currentNode.getIndex(), 11);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempLoad);
                            tempLoad.setPrev(tempLoadI);
                            tempLoad.setNext(currentNode);
                            currentNode.setPrev(tempLoad);

                            PRToVR[currPR] = currentNode.getVR(1);
                            VRToPR[currentNode.getVR(1)] = currPR;
                            PRNU[currPR] = currentNode.getNU(1);
                            currentNode.setOperands(currPR, 2);
                        }

                        // use
                        if (VRToPR[currentNode.getVR(3)] != -1) {
                            currentNode.setOperands(VRToPR[currentNode.getVR(3)], 10);
                        } else {
                            if (!PRStack.empty()) {
                                currPR = PRStack.pop();
                            } else {
                                currLastPRNU = pickLastNU(PRNU);
                                VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                                VRToPR[PRToVR[currLastPRNU]] = -1;

                                IRNode tempLoadI = new IRNode();
                                tempLoadI.setOpType(1, 0);
                                tempLoadI.setOperands(currSpillLoc, 0);
                                tempLoadI.setOperands(numRegisters - 1, 10);
                                tempLoadI.setOperands(currentNode.getIndex(), 11);


                                IRNode tempStore = new IRNode();
                                tempStore.setOpType(0, 1);
                                tempStore.setOperands(PRToVR[currLastPRNU], 1);
                                tempStore.setOperands(currLastPRNU, 2);
                                tempStore.setOperands(numRegisters - 1, 10);

                                currentNode.getPrev().setNext(tempLoadI);
                                tempLoadI.setPrev(currentNode.getPrev());
                                tempLoadI.setNext(tempStore);
                                tempStore.setPrev(tempLoadI);
                                tempStore.setNext(currentNode);
                                currentNode.setPrev(tempStore);
                                currSpillLoc += 4;
                                currPR = currLastPRNU;

                            }

                            PRToVR[currPR] = currentNode.getVR(3);
                            VRToPR[currentNode.getVR(3)] = currPR;
                            PRNU[currPR] = currentNode.getNU(3);
                            currentNode.setOperands(currPR, 10);

                            IRNode tempLoadI = new IRNode();
                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(VRToSpillLocation[currentNode.getVR(3)], 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);

                            IRNode tempLoad = new IRNode();
                            tempLoad.setOpType(0, 0);
                            tempLoad.setOperands(numRegisters - 1, 2);
                            tempLoad.setOperands(currentNode.getVR(3), 9);

                            tempLoad.setOperands(currPR, 10);
                            tempLoad.setOperands(currentNode.getIndex(), 11);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempLoad);
                            tempLoad.setPrev(tempLoadI);
                            tempLoad.setNext(currentNode);
                            currentNode.setPrev(tempLoad);

                        }

                        if (currentNode.getNU(1) == -1 && !(PRToVR[currentNode.getPR(1)] == -1)) {
                            VRToPR[PRToVR[currentNode.getPR(1)]] = -1;
                            PRToVR[currentNode.getPR(1)] = -1;
                            PRNU[currentNode.getPR(1)] = -1;
                            PRStack.push(currentNode.getPR(1));
                        }

                        if (currentNode.getNU(3) == -1 && !(PRToVR[currentNode.getPR(3)] == -1)) {
                            VRToPR[PRToVR[currentNode.getPR(3)]] = -1;
                            PRToVR[currentNode.getPR(3)] = -1;
                            PRNU[currentNode.getPR(3)] = -1;
                            PRStack.push(currentNode.getPR(3));
                        }
                    }
                }
                case 1 -> {
                    // loadI

                    // def
                    if (!PRStack.empty()) {
                        currPR = PRStack.pop();
                    } else {
                        currLastPRNU = pickLastNU(PRNU);
                        VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                        VRToPR[PRToVR[currLastPRNU]] = -1;

                        IRNode tempLoadI = new IRNode();
                        tempLoadI.setOpType(1, 0);
                        tempLoadI.setOperands(currSpillLoc, 0);
                        tempLoadI.setOperands(numRegisters - 1, 10);
                        tempLoadI.setOperands(currentNode.getIndex(), 11);


                        IRNode tempStore = new IRNode();
                        tempStore.setOpType(0, 1);
                        tempStore.setOperands(PRToVR[currLastPRNU], 1);
                        tempStore.setOperands(currLastPRNU, 2);
                        tempStore.setOperands(numRegisters - 1, 10);

                        currentNode.getPrev().setNext(tempLoadI);
                        tempLoadI.setPrev(currentNode.getPrev());
                        tempLoadI.setNext(tempStore);
                        tempStore.setPrev(tempLoadI);
                        tempStore.setNext(currentNode);
                        currentNode.setPrev(tempStore);
                        currSpillLoc += 4;
                        currPR = currLastPRNU;
                    }

                    PRToVR[currPR] = currentNode.getVR(3);
                    VRToPR[currentNode.getVR(3)] = currPR;
                    PRNU[VRToPR[currentNode.getVR(3)]] = currentNode.getNU(3);
                    currentNode.setOperands(currPR, 10);

                }
                case 2 -> {
                    // arith

                    // use
                    if (VRToPR[currentNode.getVR(1)] != -1) {
                        currentNode.setOperands(VRToPR[currentNode.getVR(1)], 2);
                    } else {
                        if (!PRStack.empty()) {
                            currPR = PRStack.pop();
                        } else {
                            currLastPRNU = pickLastNU(PRNU);
                            VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                            VRToPR[PRToVR[currLastPRNU]] = -1;

                            IRNode tempLoadI = new IRNode();
                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(currSpillLoc, 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);


                            IRNode tempStore = new IRNode();
                            tempStore.setOpType(0, 1);
                            tempStore.setOperands(PRToVR[currLastPRNU], 1);
                            tempStore.setOperands(currLastPRNU, 2);
                            tempStore.setOperands(numRegisters - 1, 10);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempStore);
                            tempStore.setPrev(tempLoadI);
                            tempStore.setNext(currentNode);
                            currentNode.setPrev(tempStore);
                            currSpillLoc += 4;
                            currPR = currLastPRNU;

                        }

                        PRToVR[currPR] = currentNode.getVR(1);
                        VRToPR[currentNode.getVR(1)] = currPR;
                        PRNU[currPR] = currentNode.getNU(1);
                        currentNode.setOperands(currPR, 2);

                        IRNode tempLoadI = new IRNode();
                        tempLoadI.setOpType(1, 0);
                        tempLoadI.setOperands(VRToSpillLocation[currentNode.getVR(1)], 0);
                        tempLoadI.setOperands(numRegisters - 1, 10);
                        tempLoadI.setOperands(currentNode.getIndex(), 11);

                        IRNode tempLoad = new IRNode();
                        tempLoad.setOpType(0, 0);
                        tempLoad.setOperands(numRegisters - 1, 2);
                        tempLoad.setOperands(currentNode.getVR(1), 9);

                        tempLoad.setOperands(currPR, 10);
                        tempLoad.setOperands(currentNode.getIndex(), 11);

                        currentNode.getPrev().setNext(tempLoadI);
                        tempLoadI.setPrev(currentNode.getPrev());
                        tempLoadI.setNext(tempLoad);
                        tempLoad.setPrev(tempLoadI);
                        tempLoad.setNext(currentNode);
                        currentNode.setPrev(tempLoad);
                    }

                    // use
                    if (VRToPR[currentNode.getVR(2)] != -1) {
                        currentNode.setOperands(VRToPR[currentNode.getVR(2)], 6);
                    } else {
                        if (!PRStack.empty()) {
                            currPR = PRStack.pop();
                        } else {
                            currLastPRNU = pickLastNU(PRNU);
                            VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                            VRToPR[PRToVR[currLastPRNU]] = -1;

                            IRNode tempLoadI = new IRNode();
                            tempLoadI.setOpType(1, 0);
                            tempLoadI.setOperands(currSpillLoc, 0);
                            tempLoadI.setOperands(numRegisters - 1, 10);
                            tempLoadI.setOperands(currentNode.getIndex(), 11);


                            IRNode tempStore = new IRNode();
                            tempStore.setOpType(0, 1);
                            tempStore.setOperands(PRToVR[currLastPRNU], 1);
                            tempStore.setOperands(currLastPRNU, 2);
                            tempStore.setOperands(numRegisters - 1, 10);

                            currentNode.getPrev().setNext(tempLoadI);
                            tempLoadI.setPrev(currentNode.getPrev());
                            tempLoadI.setNext(tempStore);
                            tempStore.setPrev(tempLoadI);
                            tempStore.setNext(currentNode);
                            currentNode.setPrev(tempStore);
                            currSpillLoc += 4;
                            currPR = currLastPRNU;

                        }

                        PRToVR[currPR] = currentNode.getVR(2);
                        VRToPR[currentNode.getVR(2)] = currPR;
                        PRNU[currPR] = currentNode.getNU(2);
                        currentNode.setOperands(currPR, 6);

                        IRNode tempLoadI = new IRNode();
                        tempLoadI.setOpType(1, 0);
                        tempLoadI.setOperands(VRToSpillLocation[currentNode.getVR(2)], 0);
                        tempLoadI.setOperands(numRegisters - 1, 10);
                        tempLoadI.setOperands(currentNode.getIndex(), 11);

                        IRNode tempLoad = new IRNode();
                        tempLoad.setOpType(0, 0);
                        tempLoad.setOperands(numRegisters - 1, 2);
                        tempLoad.setOperands(currentNode.getVR(2), 9);

                        tempLoad.setOperands(currPR, 10);
                        tempLoad.setOperands(currentNode.getIndex(), 11);

                        currentNode.getPrev().setNext(tempLoadI);
                        tempLoadI.setPrev(currentNode.getPrev());
                        tempLoadI.setNext(tempLoad);
                        tempLoad.setPrev(tempLoadI);
                        tempLoad.setNext(currentNode);
                        currentNode.setPrev(tempLoad);
                    }

                    if (currentNode.getNU(1) == -1 && !(PRToVR[currentNode.getPR(1)] == -1)) {
                        VRToPR[PRToVR[currentNode.getPR(1)]] = -1;
                        PRToVR[currentNode.getPR(1)] = -1;
                        PRNU[currentNode.getPR(1)] = -1;
                        PRStack.push(currentNode.getPR(1));
                    }

                    if (currentNode.getNU(2) == -1 && !(PRToVR[currentNode.getPR(2)] == -1)) {
                        VRToPR[PRToVR[currentNode.getPR(2)]] = -1;
                        PRToVR[currentNode.getPR(2)] = -1;
                        PRNU[currentNode.getPR(2)] = -1;
                        PRStack.push(currentNode.getPR(2));
                    }

                    // def

                    if (!PRStack.empty()) {
                        currPR = PRStack.pop();
                    } else {
                        currLastPRNU = pickLastNU(PRNU);
                        VRToSpillLocation[PRToVR[currLastPRNU]] = currSpillLoc;
                        VRToPR[PRToVR[currLastPRNU]] = -1;

                        IRNode tempLoadI = new IRNode();
                        tempLoadI.setOpType(1, 0);
                        tempLoadI.setOperands(currSpillLoc, 0);
                        tempLoadI.setOperands(numRegisters - 1, 10);
                        tempLoadI.setOperands(currentNode.getIndex(), 11);


                        IRNode tempStore = new IRNode();
                        tempStore.setOpType(0, 1);
                        tempStore.setOperands(PRToVR[currLastPRNU], 1);
                        tempStore.setOperands(currLastPRNU, 2);
                        tempStore.setOperands(numRegisters - 1, 10);

                        currentNode.getPrev().setNext(tempLoadI);
                        tempLoadI.setPrev(currentNode.getPrev());
                        tempLoadI.setNext(tempStore);
                        tempStore.setPrev(tempLoadI);
                        tempStore.setNext(currentNode);
                        currentNode.setPrev(tempStore);
                        currSpillLoc += 4;
                        currPR = currLastPRNU;
                    }

                    PRToVR[currPR] = currentNode.getVR(3);
                    VRToPR[currentNode.getVR(3)] = currPR;
                    currentNode.setOperands(currPR, 10);
                    PRNU[VRToPR[currentNode.getVR(3)]] = currentNode.getNU(3);
                }
            }
            currentNode = currentNode.getNext();
        }
    }

    private static int pickLastNU(int[] PRNU) {
        int max = 0;
        for (int i = 0; i < PRNU.length; i++) {
            if (PRNU[i] > PRNU[max]) {
                max = i;
            }
        }
        return max;
    }
}
