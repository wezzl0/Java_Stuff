import java.util.*;

public class Main {

    HashMap<String, Integer> symbolTable = new HashMap<>();
    List<byte[]> dataToWrite = new LinkedList<>();
    int origin = -1;
    String filePath;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Assembler needs the file path as an argument.");
            System.exit(1);
        }
        new Main(args[0]);
    }

    Main(String path) {
        this.filePath = path;
        getSymbolTable(); // first pass
        writeData(); // Second pass, incomplete
    }

    private void getSymbolTable() {
        // Init scanner
        Scanner scanner = Util.getScanner(filePath);
        if (!scanner.hasNextLine()) {
            System.err.println("File is empty.");
            System.exit(2);
        }
        List<Token> awaitingAdditionToSymbolTable = new LinkedList<>();
        int addressCounter = 0;
        while (scanner.hasNextLine()) {
            String line = Util.stripComment(scanner.nextLine());
            String[] splitLine = line.split("[\\s]+|,");
            List<Token> tokens = new LinkedList<>();
            // Tokenize all strings except for blank strings
            for (String str : splitLine) {
                if (str.length() == 0) {
                    continue;
                }
                tokens.add(Token.tokenize(str));
            }
            if (tokens.size() == 0) {
                continue;
            }
            // Grab origin if not previously gotten.
            if (this.origin == -1) {
                if (tokens.get(0).info.equals(".orig") && tokens.get(1).type == Token.Type.LITERAL) {
                    int value = Integer.parseInt(tokens.get(1).info);
                    // Make sure origin does not overwrite OS
                    if (value < 12288 || value > 65023) {
                        Util.error();
                    }
                    origin = value;
                    addressCounter += origin;
                    continue;
                } else {
                    // No origin error
                    Util.error();
                }
            }
            // This section removes leading labels, puts them in a list,
            // and waits for a line with data to address to.
            Token firstToken = tokens.remove(0);
            boolean containsTokensOtherThanLabel = tokens.size() >= 1;
            if (firstToken.type == Token.Type.LABEL) {
                awaitingAdditionToSymbolTable.add(firstToken);
            }
            if (containsTokensOtherThanLabel) {
                ListIterator<Token> iter = awaitingAdditionToSymbolTable.listIterator();
                while (iter.hasNext()) {
                    Token add = awaitingAdditionToSymbolTable.remove(0);
                    symbolTable.put(add.info, addressCounter);
                }
                addressCounter++;
            }
        }
    }

    private void writeData() {
        Scanner scanner = Util.getScanner(filePath);
		//TODO
    }
}




