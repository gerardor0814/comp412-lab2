import java.util.Arrays;
import java.util.Stack;

public class Allocator {
    private final IRNode tail;
    private final IRNode head;
    private int VRName;

    public Allocator(IRNode tail, IRNode head) {
        this.tail = tail;
        this.head = head;
        this.VRName = -1;

    }

    public void rename() {

        int[] SRToVR = new int[tail.getIndex()];
        int[] LastUsed = new int[tail.getIndex()];
        int index = tail.getIndex();
        for (int i = 0; i < tail.getIndex(); i++) {
            SRToVR[i] = -1;
            LastUsed[i] = Integer.MAX_VALUE;
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
                        LastUsed[currentNode.getSR(3)] = Integer.MAX_VALUE;

                        // use
                        if (SRToVR[currentNode.getSR(1)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(1)] = VRName;
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
                        }
                        currentNode.setOperands(SRToVR[currentNode.getSR(1)], 1);
                        currentNode.setOperands(LastUsed[currentNode.getSR(1)], 3);

                        //use
                        if (SRToVR[currentNode.getSR(3)] == -1) {
                            VRName++;
                            SRToVR[currentNode.getSR(3)] = VRName;
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
                    LastUsed[currentNode.getSR(3)] = Integer.MAX_VALUE;
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
                    LastUsed[currentNode.getSR(3)] = Integer.MAX_VALUE;

                    // use
                    if (SRToVR[currentNode.getSR(1)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(1)] = VRName;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(1)], 1);
                    currentNode.setOperands(LastUsed[currentNode.getSR(1)], 3);

                    // use
                    if (SRToVR[currentNode.getSR(2)] == -1) {
                        VRName++;
                        SRToVR[currentNode.getSR(2)] = VRName;
                    }
                    currentNode.setOperands(SRToVR[currentNode.getSR(2)], 5);
                    currentNode.setOperands(LastUsed[currentNode.getSR(2)], 7);

                    LastUsed[currentNode.getSR(1)] = index;
                    LastUsed[currentNode.getSR(2)] = index;
                }
            }
            currentNode = currentNode.getPrev();
            index--;
        }
    }

    public void allocate(int numRegisters) {
        int[] VRToPR = new int[this.VRName + 1];
        int[] PRToVR = new int[numRegisters];
        Stack<Integer> PRStack = new Stack<>();
        int currPR = -1;

        for(int i = numRegisters - 1; i > 0; i--) {
            PRToVR[i] = -1;
            PRStack.push(i);
        }

        IRNode currentNode = this.head;
        while (currentNode != null) {
            switch (currentNode.getOpCategory()) {
                case 0 -> {
                    // load
                    if (currentNode.getOpCode() == 0) {
                        // use



                        // def


                    } else {
                        // store

                        //use

                        //use

                    }
                }
                case 1 -> {
                    // loadI

                    // def
                    if (!PRStack.empty()) {
                        currPR = PRStack.pop();
                    }

                    PRToVR[currPR] = currentNode.getVR(3);
                    VRToPR[currentNode.getVR(3)] = currPR;

                    currentNode.setOperands(currPR, 10);
                }
                case 2 -> {
                    // arith
                    // def

                    // use

                    // use

                }
            }
            currentNode = currentNode.getNext();
        }

    }
}