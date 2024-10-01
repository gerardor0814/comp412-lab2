/**
 * 0 - MEMOP: load, store
 * 1 - LOADI: loadI
 * 2 - ARITHOP: add, sub, mult, lshift, rshift
 * 3 - OUTPUT: output
 * 4 - NOP: nop
 * 5 - CONSTANT: a non-negative integer
 * 6 - REGISTER: ‘r’ followed by a constant
 * 7 - COMMA: ‘,’
 * 8 - INTO: “=>”
 * 9 - EOF: input has been exhausted
 * 10 - EOL: end of the current line
 */

public class IRNode {
    int[] operands;
    private int line;
    private int opCategory;
    private int opCode;
    private IRNode next;
    private IRNode prev;

    public IRNode() {
        operands = new int[12];
    }

    public void setNext(IRNode next) {
        this.next = next;
    }

    public IRNode getNext() {
        return next;
    }

    public void setPrev(IRNode prev) {
        this.prev = prev;
    }

    public IRNode getPrev() {
        return prev;
    }

    public void setOperands(int value, int index) {
        operands[index] = value;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setOpType(int opCategory, int opCode) {
        this.opCategory = opCategory;
        this.opCode = opCode;
    }

    @Override
    public String toString() {
        String operation = "";
        String body = "";
        switch (this.opCategory) {
            case 0 -> {
                if (this.opCode == 0) {
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
                if (this.opCode == 0) {
                    operation = "add     ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (this.opCode == 1) {
                    operation = "sub     ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (this.opCode == 2) {
                    operation = "mult    ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (this.opCode == 3) {
                    operation = "lshift  ";
                    body = "[ sr" + this.operands[0] + " ], [ sr" + this.operands[4] + " ], [ sr" + this.operands[8] + " ]";
                } else if (this.opCode == 4) {
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
        }
        return operation + body;
    }
}
