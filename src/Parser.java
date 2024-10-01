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

public class Parser {

    private Scanner scanner;
    private IRNode head;
    private IRNode tail;
    private boolean valid;
    private int count;



    public Parser(Scanner scanner) {
        this.scanner = scanner;
        head = new IRNode();
        valid = true;
        count = 0;
    }

    public void parseP() {
        this.parse();

        if (scanner.hasErrors() || !this.valid) {
            System.err.println("Parse found errors");
        } else {
            System.out.println("Parse succeeded. Processed " + count + " operations");
        }
    }

    public void parseR() {
        this.parse();
        IRNode currentNode = head;
        if (!scanner.hasErrors() && this.valid){
            while (currentNode != null) {
                System.out.println(currentNode);
                currentNode = currentNode.getNext();
            }
        } else {
            System.err.println("Due to syntax errors, run terminates");
        }

    }

    public void parse() {
        Trio currentWord;
        IRNode currentNode = head;
        boolean eof = false;
        while (!eof) {
            currentWord = scanner.getNextWord();
            currentNode.setLine(scanner.getCurrentLineIndex());
            switch (currentWord.Category()) {
                case 0 -> {
                    if (currentWord.Words() == 0) {
                        currentNode.setOpType(0, 0);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 8) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 8);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() != 10) {
                                        System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in load operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    } else {
                                        currentNode.setNext(new IRNode());
                                        currentNode.getNext().setPrev(currentNode);
                                        currentNode = currentNode.getNext();
                                        count++;
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing target register in load operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in load operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing source register in load operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else {
                        currentWord = scanner.getNextWord();
                        currentNode.setOpType(0, 1);
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 8) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 8);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() != 10) {
                                        System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in store operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    } else {
                                        currentNode.setNext(new IRNode());
                                        currentNode.getNext().setPrev(currentNode);
                                        currentNode = currentNode.getNext();
                                        count++;
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing target register in store operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in store operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing source register in store operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    }
                }
                case 1 -> {
                    currentNode.setOpType(1, 0);
                    currentWord = scanner.getNextWord();
                    if (currentWord.Category() == 5) {
                        currentNode.setOperands(currentWord.Words(), 0);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 8) {
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 6) {
                                currentNode.setOperands(currentWord.Words(), 8);
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() != 10) {
                                    System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in loadI operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                } else {
                                    currentNode.setNext(new IRNode());
                                    currentNode.getNext().setPrev(currentNode);
                                    currentNode = currentNode.getNext();
                                    count++;
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing target register in loadI operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in loadI operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else {
                        System.err.println("ERROR " + currentWord.Line() + ": Missing constant in loadI operation");
                        this.valid = false;
                        scanner.getNextLine();
                    }
                }
                case 2 -> {
                    if (currentWord.Words() == 0) {
                        currentNode.setOpType(2, 0);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 7) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 4);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() == 8) {
                                        currentWord = scanner.getNextWord();
                                        if (currentWord.Category() == 6) {
                                            currentNode.setOperands(currentWord.Words(), 8);
                                            currentWord = scanner.getNextWord();
                                            if (currentWord.Category() != 10) {
                                                System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in add operation");
                                                this.valid = false;
                                                scanner.getNextLine();
                                            } else {
                                                currentNode.setNext(new IRNode());
                                                currentNode.getNext().setPrev(currentNode);
                                                currentNode = currentNode.getNext();
                                                count++;
                                            }
                                        } else {
                                            System.err.println("ERROR " + currentWord.Line() + ": Missing target register in add operation");
                                            this.valid = false;
                                            scanner.getNextLine();
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in add operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing second source register in add operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing comma between source registers in add operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing first source register in add operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else if (currentWord.Words() == 1) {
                        currentNode.setOpType(2, 1);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 7) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 4);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() == 8) {
                                        currentWord = scanner.getNextWord();
                                        if (currentWord.Category() == 6) {
                                            currentNode.setOperands(currentWord.Words(), 8);
                                            currentWord = scanner.getNextWord();
                                            if (currentWord.Category() != 10) {
                                                System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in sub operation");
                                                this.valid = false;
                                                scanner.getNextLine();
                                            } else {
                                                currentNode.setNext(new IRNode());
                                                currentNode.getNext().setPrev(currentNode);
                                                currentNode = currentNode.getNext();
                                                count++;
                                            }
                                        } else {
                                            System.err.println("ERROR " + currentWord.Line() + ": Missing target register in sub operation");
                                            this.valid = false;
                                            scanner.getNextLine();
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in sub operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing second source register in sub operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing comma between source registers in sub operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing first source register in sub operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else if (currentWord.Words() == 2) {
                        currentNode.setOpType(2, 2);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 7) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 4);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() == 8) {
                                        currentWord = scanner.getNextWord();
                                        if (currentWord.Category() == 6) {
                                            currentNode.setOperands(currentWord.Words(), 8);
                                            currentWord = scanner.getNextWord();
                                            if (currentWord.Category() != 10) {
                                                System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in mult operation");
                                                this.valid = false;
                                                scanner.getNextLine();
                                            } else {
                                                currentNode.setNext(new IRNode());
                                                currentNode.getNext().setPrev(currentNode);
                                                currentNode = currentNode.getNext();
                                                count++;
                                            }
                                        } else {
                                            System.err.println("ERROR " + currentWord.Line() + ": Missing target register in mult operation");
                                            this.valid = false;
                                            scanner.getNextLine();
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in mult operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing second source register in mult operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing comma between source registers in mult operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing first source register in mult operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else if (currentWord.Words() == 3) {
                        currentNode.setOpType(2, 3);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 7) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 4);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() == 8) {
                                        currentWord = scanner.getNextWord();
                                        if (currentWord.Category() == 6) {
                                            currentNode.setOperands(currentWord.Words(), 8);
                                            currentWord = scanner.getNextWord();
                                            if (currentWord.Category() != 10) {
                                                System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in lshift operation");
                                                this.valid = false;
                                                scanner.getNextLine();
                                            } else {
                                                currentNode.setNext(new IRNode());
                                                currentNode.getNext().setPrev(currentNode);
                                                currentNode = currentNode.getNext();
                                                count++;
                                            }
                                        } else {
                                            System.err.println("ERROR " + currentWord.Line() + ": Missing target register in lshift operation");
                                            this.valid = false;
                                            scanner.getNextLine();
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in lshift operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing second source register in lshift operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing comma between source registers in lshift operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing first source register in lshift operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    } else {
                        currentNode.setOpType(2, 4);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() == 6) {
                            currentNode.setOperands(currentWord.Words(), 0);
                            currentWord = scanner.getNextWord();
                            if (currentWord.Category() == 7) {
                                currentWord = scanner.getNextWord();
                                if (currentWord.Category() == 6) {
                                    currentNode.setOperands(currentWord.Words(), 4);
                                    currentWord = scanner.getNextWord();
                                    if (currentWord.Category() == 8) {
                                        currentWord = scanner.getNextWord();
                                        if (currentWord.Category() == 6) {
                                            currentNode.setOperands(currentWord.Words(), 8);
                                            currentWord = scanner.getNextWord();
                                            if (currentWord.Category() != 10) {
                                                System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in rshift operation");
                                                this.valid = false;
                                                scanner.getNextLine();
                                            } else {
                                                currentNode.setNext(new IRNode());
                                                currentNode.getNext().setPrev(currentNode);
                                                currentNode = currentNode.getNext();
                                                count++;
                                            }
                                        } else {
                                            System.err.println("ERROR " + currentWord.Line() + ": Missing target register in rshift operation");
                                            this.valid = false;
                                            scanner.getNextLine();
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentWord.Line() + ": Missing into symbol in rshift operation");
                                        this.valid = false;
                                        scanner.getNextLine();
                                    }
                                } else {
                                    System.err.println("ERROR " + currentWord.Line() + ": Missing second source register in rshift operation");
                                    this.valid = false;
                                    scanner.getNextLine();
                                }
                            } else {
                                System.err.println("ERROR " + currentWord.Line() + ": Missing comma between source registers in rshift operation");
                                this.valid = false;
                                scanner.getNextLine();
                            }
                        } else {
                            System.err.println("ERROR " + currentWord.Line() + ": Missing first source register in rshift operation");
                            this.valid = false;
                            scanner.getNextLine();
                        }
                    }

                }
                case 3 -> {
                    currentNode.setOpType(3, 0);
                    currentWord = scanner.getNextWord();
                    if (currentWord.Category() == 5) {
                        currentNode.setOperands(currentWord.Words(), 0);
                        currentWord = scanner.getNextWord();
                        if (currentWord.Category() != 10) {
                            System.err.println("ERROR " + currentWord.Line() + ": Extraneous argument in the output operation");
                            this.valid = false;
                            scanner.getNextLine();
                        } else {
                            currentNode.setNext(new IRNode());
                            currentNode.getNext().setPrev(currentNode);
                            currentNode = currentNode.getNext();
                            count++;
                        }
                    } else {
                        System.err.println("ERROR " + currentWord.Line() + ": Missing constant in output operation");
                        this.valid = false;
                        scanner.getNextLine();
                    }
                }
                case 4 -> {
                    currentNode.setOpType(4, 0);
                    currentWord = scanner.getNextWord();
                    if (currentWord.Category() != 10) {
                        System.err.println("ERROR " + currentWord.Line() + ": extraneous argument in the nop operation");
                        this.valid = false;
                        scanner.getNextLine();
                    } else {
                        currentNode.setNext(new IRNode());
                        currentNode.getNext().setPrev(currentNode);
                        currentNode = currentNode.getNext();
                        count++;
                    }
                }
                case 9 -> {
                    eof = true;
                    if (currentNode != this.head) {
                        this.tail = currentNode.getPrev();
                        this.tail.setNext(null);
                    }
                }
                case 10 -> {
                    continue;
                }
                default -> {
                    System.err.println("ERROR " + currentWord.Line() + ": illegal start to a line");
                    this.valid = false;
                    scanner.getNextLine();
                }
            }
        }
    }
}
