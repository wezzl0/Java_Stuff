import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class Util {

    static Scanner getScanner(String filePath) {
        try {
            return new Scanner(new File(filePath));
        } catch (IOException e) {
            System.out.println("FILE READ ERROR.");
            System.exit(1);
        }
        return null;
    }

    // Gets a string of all characters before the first ";"
    static String stripComment(String str) {
        return str.split(";", 2)[0];
    }

    static void error() {
    }

    // Given hex strings must be even
    private static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    // Adds zeros to hex strings to make them even and so we don't get a "one off" error, ex.) f -> 000f
    private static String formatHex(String hex) {
        int zeroToAdd = 4 - hex.length();
        StringBuilder newHex = new StringBuilder(hex);
        for (int i = 0; i < zeroToAdd; i++) {
            newHex.insert(0, "0");
        }
        return newHex.toString();
    }

}
