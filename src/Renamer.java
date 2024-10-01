public class Renamer {
    private final IRNode tail;

    public Renamer(IRNode tail){
        this.tail = tail;
    }

    public void rename(){
        int VRName = 0;

        int[] SRToVR = new int[tail.getIndex()];
        int[] LastUsed = new int[tail.getIndex()];
        int index = tail.getIndex();
        for (int i = 0; i < tail.getIndex(); i++) {
            SRToVR[i] = -1;
            LastUsed[i] = Integer.MAX_VALUE;
        }

        IRNode currentNode = tail;
        while (currentNode != null) {
            case 0 -> {
                if (currentNode.getOpCode() == 0) {
                    operation = "load    ";
                } else {
                    operation = "store   ";
                }
                body = "[ sr" + this.operands[0] + " ], [ ], [ sr" + this.operands[8] + " ]";
            }
            case 1 -> {
                operation = "loadI   ";
                body = "[ val " + this.operands[0] + " ], [ ], [ sr" + this.operands[8] + " ]";
            }
            case 2 -> {
                if (currentNode.getOpCode() == 0) {
                    operation = "add     ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (currentNode.getOpCode() == 1) {
                    operation = "sub     ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (currentNode.getOpCode() == 2) {
                    operation = "mult    ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (currentNode.getOpCode() == 3) {
                    operation = "lshift  ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (currentNode.getOpCode() == 4) {
                    operation = "rshift  ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                }
            }
            case 3 -> {
                operation = "output  ";
                body = "[ val " + this.operands[0] + " ], [ ], [ ]";
            }
            case 4 -> {
                operation = "nop     ";
                body = "[ ], [ ], [ ]";
            }
            currentNode = currentNode.getPrev();

        }
    }
}
