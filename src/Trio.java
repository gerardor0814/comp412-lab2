public record Trio(int Category, int Words, int Line) {

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

    @Override
    public String toString() {
        String category = "";
        String words = "";
        switch (Category) {
            case 0 -> {
                category = ": < MEMOP, ";
                if (Words == 0) {
                    words = "\"load\" >\n";
                } else {
                    words = "\"store\" >\n";
                }
            }
            case 1 -> {
                category = ": < LOADI, ";
                words = "\"loadI\" >\n";
            }
            case 2 -> {
                category = ": < ARITHOP, ";
                if (Words == 0) {
                    words = "\"add\" >\n";
                } else if (Words == 1) {
                    words = "\"sub\" >\n";
                } else if (Words == 2) {
                    words = "\"mult\" >\n";
                } else if (Words == 3) {
                    words = "\"lshift\" >\n";
                } else if (Words == 4) {
                    words = "\"rshift\" >\n";
                }
            }
            case 3 -> {
                category = ": < OUTPUT, ";
                words = "\"output\" >\n";
            }
            case 4 -> {
                category = ": < NOP, ";
                words = "\"nop\" >\n";
            }
            case 5 -> {
                category = ": < CONST, ";
                words = "\"" + Words + "\" >\n";
            }
            case 6 -> {
                category = ": < REG, ";
                words = "\"r" + Words + "\" >\n";
            }
            case 7 -> {
                category = ": < COMMA, ";
                words = "\",\" >\n";
            }
            case 8 -> {
                category = ": < INTO, ";
                words = "\"=>\" >\n";
            }
            case 9 -> {
                category = ": < ENDFILE, ";
                words = "\"\" >\n";
            }
            case 10 -> {
                category = ": < NEWLINE, ";
                words = "\"\\n\" >\n";
            }
            case 11 -> {
                return "";
            }
        }
        return Line + category + words;
    }

    public boolean isEOF() {
        return Category == 9;
    }
}
