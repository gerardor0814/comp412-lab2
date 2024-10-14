public class Main {
    public static void main(String[] args) {
        String helpStatement = """
                Command Syntax:
                        ./412alloc k filename

                Required arguments:
                        filename  is the pathname (absolute or relative) to the input file
                        k         specifies the number of registers available to the allocator

                At most one of the following two flags:
                        -h        prints this message
                        -x        only for code check 1, scans, parses and prints renamed registers""";
        String fileName = "";
        int option = 0;
        int numRegisters = -1;
        for (String arg : args) {
            switch (arg) {
                case "-h" -> {
                    System.out.println(helpStatement);
                    return;
                }
                case "-x" ->
                    option = 1;
                default -> {
                    if (numRegisters == -1) {
                        try {
                            numRegisters = Integer.parseInt(arg);
                            // is an integer!
                        } catch (NumberFormatException e) {
                            if (fileName.isEmpty()) {
                                fileName = arg;
                            } else {
                                System.out.println("ERROR:  Attempt to open more than one input file.\n");
                                System.out.println(helpStatement);
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (numRegisters < 3 && option == 0) {
            System.out.println("ERROR:  (Fatal) minimum value for k is 3.");
            System.out.println("Allocation failed.");
            return;
        }
        Scanner scanner = new Scanner(fileName);
        if (scanner.hasErrors()) {
            return;
        }
        Parser parser = new Parser(scanner);

        if (option == 1) {
            runXFlag(parser);
        } else {
            run(parser, numRegisters);
        }

    }

    public static void run(Parser parser, int numRegisters) {
        parser.parse();
        Allocator allocator = new Allocator(parser.getTail(), parser.getHead());
        IRNode currentNode = parser.getHead();
        if (parser.isValid()) {
            allocator.rename();
            allocator.allocate(4);
            while (currentNode != null) {
                System.out.println(currentNode.reallocatedString());
                currentNode = currentNode.getNext();
            }
        } else {
            System.err.println("Due to syntax errors, run terminates");
        }
    }

    public static void runXFlag(Parser parser) {
        parser.parse();
        Allocator allocator = new Allocator(parser.getTail(), parser.getHead());
        IRNode currentNode = parser.getHead();
        if (parser.isValid()) {
            allocator.rename();
            while (currentNode != null) {
                System.out.println(currentNode.rewrittenString());
                currentNode = currentNode.getNext();
            }
        } else {
            System.err.println("Due to syntax errors, run terminates");
        }
    }
}