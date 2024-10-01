import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Scanner {

    private BufferedReader bufferedReader;
    private int currentIndex;
    FileReader fileReader;
    private String currentLine;
    private int currentLineIndex;
    private int currentLineLength;
    private boolean hasErrors;

    public Scanner(String fileName) {

        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Could not open file " + fileName + " as the input file.");
            hasErrors = true;
            return;
        }
        bufferedReader = new BufferedReader(fileReader);
        currentLineIndex = 0;
        this.getNextLine();
        currentIndex = 0;
        hasErrors = false;
    }

    public Trio getNextWord() {
        if (currentIndex >= currentLineLength) {
            this.getNextLine();
            if (this.currentLine == null) {
                return new Trio(9, 0, currentLineIndex);
            }
        }
        while (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
            currentIndex++;
            if (currentIndex == currentLineLength) {
                return new Trio(10, 0, currentLineIndex);
            }
        }

            if (currentLine.charAt(currentIndex) == '/') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == '/') {
                    this.getNextLine();
                    return new Trio(10,0, currentLineIndex - 1);
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"/" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == 's') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == 't') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'o') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == 'r') {
                            currentIndex++;
                            if (currentLine.charAt(currentIndex) == 'e') {
                                currentIndex++;
                                if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                    currentIndex++;
                                    return new Trio(0, 1, currentLineIndex);
                                }
                                else {
                                    System.err.println("ERROR " + currentLineIndex + ": No whitespace after store operation");
                                    hasErrors = true;
                                    currentIndex = currentLineLength;
                                }
                            } else {
                                System.err.println("ERROR " + currentLineIndex + ": \"stor" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                hasErrors = true;
                                currentIndex = currentLineLength;
                            }
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": \"sto" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"st" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else if (currentLine.charAt(currentIndex) == 'u') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'b') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                            currentIndex++;
                            return new Trio(2, 1, currentLineIndex);
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": No whitespace after sub operation");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"su" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"s" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == 'l') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'o') {
                         currentIndex++;
                         if (currentLine.charAt(currentIndex) == 'a') {
                             currentIndex++;
                             if (currentLine.charAt(currentIndex) == 'd') {
                                 currentIndex++;
                                 if (currentLine.charAt(currentIndex) == 'I') {
                                     currentIndex++;
                                     if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                         currentIndex++;
                                         return new Trio(1, 0, currentLineIndex);
                                     } else {
                                         System.err.println("ERROR " + currentLineIndex + ": No whitespace after loadI operation");
                                         hasErrors = true;
                                         currentIndex = currentLineLength;
                                     }
                                 } else if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                     return new Trio(0, 0, currentLineIndex);
                                 } else {
                                     System.err.println("ERROR " + currentLineIndex + ": No whitespace after load operation");
                                     hasErrors = true;
                                     currentIndex = currentLineLength;
                                 }
                             } else {
                                 System.err.println("ERROR " + currentLineIndex + ": \"loa" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                 hasErrors = true;
                                 currentIndex = currentLineLength;
                             }
                         } else {
                             System.err.println("ERROR " + currentLineIndex + ": \"lo" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                             hasErrors = true;
                             currentIndex = currentLineLength;
                         }
                    } else if (currentLine.charAt(currentIndex) == 's') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == 'h') {
                            currentIndex++;
                            if (currentLine.charAt(currentIndex) == 'i') {
                                currentIndex++;
                                if (currentLine.charAt(currentIndex) == 'f') {
                                    currentIndex++;
                                    if (currentLine.charAt(currentIndex) == 't') {
                                        currentIndex++;
                                        if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                            currentIndex++;
                                            return new Trio(2, 3, currentLineIndex);
                                        } else {
                                            System.err.println("ERROR " + currentLineIndex + ": No whitespace after shift operation");
                                            hasErrors = true;
                                            currentIndex = currentLineLength;
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentLineIndex + ": \"lshif" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                        hasErrors = true;
                                        currentIndex = currentLineLength;
                                    }
                                } else {
                                    System.err.println("ERROR " + currentLineIndex + ": \"lshi" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                    hasErrors = true;
                                    currentIndex = currentLineLength;
                                }
                            } else {
                                System.err.println("ERROR " + currentLineIndex + ": \"lsh" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                hasErrors = true;
                                currentIndex = currentLineLength;
                            }
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": \"ls" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"l" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else if (currentLine.charAt(currentIndex) == 'r') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 's') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == 'h') {
                            currentIndex++;
                            if (currentLine.charAt(currentIndex) == 'i') {
                                currentIndex++;
                                if (currentLine.charAt(currentIndex) == 'f') {
                                    currentIndex++;
                                    if (currentLine.charAt(currentIndex) == 't') {
                                        currentIndex++;
                                        if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                            currentIndex++;
                                            return new Trio(2, 4, currentLineIndex);
                                        } else {
                                            System.err.println("ERROR " + currentLineIndex + ": No whitespace after rshift operation");
                                            hasErrors = true;
                                            currentIndex = currentLineLength;
                                        }
                                    } else {
                                        System.err.println("ERROR " + currentLineIndex + ": \"rshif" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                        hasErrors = true;
                                        currentIndex = currentLineLength;
                                    }
                             } else {
                                    System.err.println("ERROR " + currentLineIndex + ": \"rshi" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                    hasErrors = true;
                                    currentIndex = currentLineLength;
                                }
                        } else {
                                System.err.println("ERROR " + currentLineIndex + ": \"rsh" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                hasErrors = true;
                                currentIndex = currentLineLength;
                            }
                    } else {
                            System.err.println("ERROR " + currentLineIndex + ": \"rs" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                } else if (currentLine.charAt(currentIndex) >= '0' && currentLine.charAt(currentIndex) <= '9') {
                        int currentVal = 0;
                        int temp;
                        while (currentLine.charAt(currentIndex) >= '0' && currentLine.charAt(currentIndex) <= '9') {
                            temp = Character.getNumericValue(currentLine.charAt(currentIndex));
                            currentVal = currentVal * 10 + temp;
                            currentIndex++;
                            if (currentIndex > currentLineLength - 1) {
                                break;
                            }
                        }
                        return new Trio(6, currentVal, currentLineIndex);
                    }
            } else if (currentLine.charAt(currentIndex) == 'm') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == 'u') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'l') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == 't') {
                            currentIndex++;
                            if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t'){
                                currentIndex++;
                                return new Trio(2, 2, currentLineIndex);
                            } else {
                                System.err.println("ERROR " + currentLineIndex + ": No whitespace after mult operation");
                                hasErrors = true;
                                currentIndex = currentLineLength;
                            }
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": \"mul" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"mu" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"m" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == 'a') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == 'd') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'd') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                            currentIndex++;
                            return new Trio(2, 0, currentLineIndex);
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": No whitespace after add operation");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"ad" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"a" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == 'n') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == 'o') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 'p') {
                        currentIndex++;
                        return new Trio(4, 0, currentLineIndex);
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"no" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"n" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == 'o') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == 'u') {
                    currentIndex++;
                    if (currentLine.charAt(currentIndex) == 't') {
                        currentIndex++;
                        if (currentLine.charAt(currentIndex) == 'p') {
                            currentIndex++;
                            if (currentLine.charAt(currentIndex) == 'u') {
                                currentIndex++;
                                if (currentLine.charAt(currentIndex) == 't') {
                                    currentIndex++;
                                    if (currentLine.charAt(currentIndex) == ' ' || currentLine.charAt(currentIndex) == '\t') {
                                        currentIndex++;
                                        return new Trio(3, 0, currentLineIndex);
                                    } else {
                                        System.err.println("ERROR " + currentLineIndex + ": No whitespace after output operation");
                                        hasErrors = true;
                                        currentIndex = currentLineLength;
                                    }
                                } else {
                                    System.err.println("ERROR " + currentLineIndex + ": \"outpu" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                    hasErrors = true;
                                    currentIndex = currentLineLength;
                                }
                            } else {
                                System.err.println("ERROR " + currentLineIndex + ": \"outp" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                                hasErrors = true;
                                currentIndex = currentLineLength;
                            }
                        } else {
                            System.err.println("ERROR " + currentLineIndex + ": \"out" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                            hasErrors = true;
                            currentIndex = currentLineLength;
                        }
                    } else {
                        System.err.println("ERROR " + currentLineIndex + ": \"ou" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                        hasErrors = true;
                        currentIndex = currentLineLength;
                    }
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"o" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == '=') {
                currentIndex++;
                if (currentLine.charAt(currentIndex) == '>') {
                    currentIndex++;
                    return new Trio(8, 0, currentLineIndex);
                } else {
                    System.err.println("ERROR " + currentLineIndex + ": \"=" + currentLine.charAt(currentIndex) + "\" is not a valid word");
                    hasErrors = true;
                    currentIndex = currentLineLength;
                }
            } else if (currentLine.charAt(currentIndex) == ',') {
                currentIndex++;
                return new Trio(7, 0, currentLineIndex);

            } else if (currentLine.charAt(currentIndex) >= '0' && currentLine.charAt(currentIndex) <= '9') {
                int currentVal = 0;
                int temp;
                while (currentLine.charAt(currentIndex) >= '0' && currentLine.charAt(currentIndex) <= '9') {
                    temp = Character.getNumericValue(currentLine.charAt(currentIndex));
                    currentVal = currentVal * 10 + temp;
                    currentIndex++;
                    if (currentIndex > currentLineLength - 1) {
                        break;
                    }
                }
                return new Trio(5, currentVal, currentLineIndex);
            } else if (currentLine.charAt(currentIndex) == '\n') {
                    currentIndex++;
                    return new Trio(10, 0, currentLineIndex);
            }
            else {
                System.err.println("ERROR " + this.currentLineIndex + ": \"" + this.currentLine.charAt(this.currentIndex) + "\" is not a valid word");
                this.hasErrors = true;
                this.currentIndex = this.currentLineLength;
            }
        return new Trio(10, 0, currentLineIndex);
    }

    public void getNextLine() {
        try {
            this.currentLine = this.bufferedReader.readLine();
            this.currentLineIndex++;
        } catch (IOException e) {
            System.err.println("ERROR: Could not read next line from file");
        }
        if (this.currentLine == null) {
            currentIndex = currentLineLength;
            return;
        } else {
            this.currentLine = this.currentLine + '\n';
        }
        this.currentIndex = 0;
        this.currentLineLength = this.currentLine.length();
    }

    public boolean hasErrors() {
        return this.hasErrors;
    }

    public int getCurrentLineIndex() {
        return this.currentLineIndex;
    }
}

