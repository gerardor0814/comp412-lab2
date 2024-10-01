import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String helpStatement = """
                Command Syntax:
                        ./412alloc [flags] filename

                Required arguments:
                        filename  is the pathname (absolute or relative) to the input file

                Optional flags:
                         k       specifies the number of registers available to the allocator
                        -h       prints this message

                At most one of the following two flags:
                        -x       only for code check 1, scans, parses and 
                If none is specified, the default action is '-p'.""";
        String fileName = "";
        int option = 0;
        boolean multipleFlags = false;
        for (String arg : args) {
            switch (arg) {
                case "-h" -> {
                    System.out.println(helpStatement);
                    return;
                }
                case "-s" -> {
                    if (option != 0) {
                        multipleFlags = true;
                    } else {
                        option = 1;
                    }
                }
                case "-p" -> {
                    if (option != 0) {
                        multipleFlags = true;
                    }
                    if (option < 2) {
                        option = 2;
                    }
                }
                case "-r" -> {
                    if (option != 0) {
                        multipleFlags = true;
                    }
                    option = 3;
                }
                default -> {
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
        if (multipleFlags){
            System.out.println("ERROR:  Multiple command-line flags found.\n" +
                    "        Try '-h' for information on command-line syntax.");
        }

        if (option == 0){
            option = 2;
        }

        Scanner scanner = new Scanner(fileName);
        if (scanner.hasErrors()) {
            return;
        }
        Parser parser = new Parser(scanner);

        switch (option) {
            case 1:
                runSFlag(scanner);
                break;
            case 2:
                runPFlag(parser);
                break;
            case 3:
                runRFlag(parser);
                break;
            default:
                System.err.println("ERROR:  Unknown option.");
        }

    }

    public static void runSFlag(Scanner scanner) {
            Trio currentWord;
            do {
                currentWord = scanner.getNextWord();
                System.out.print(currentWord);
            } while (!currentWord.isEOF());
        }

    public static void runPFlag(Parser parser){
        parser.parseP();
    }

    public static void runRFlag(Parser parser){
        parser.parseR();
    }
}