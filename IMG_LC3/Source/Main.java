import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class Main {

    static LinkedList<byte[]> colorInstruction = new LinkedList<>();
    static LinkedList<byte[]> locationInstruction = new LinkedList<>();

    public static void main(String[] args) {

        if (args.length > 1) {
            System.err.println("Too many inputs given.");
            System.exit(4);
        }

        // default file name/path
        String name = "IMG.png";

        if (args.length == 1) {
            name = args[0];
        }

        readImage(name);
        writeData();
    }

    private static void readImage(String name) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(name));
        } catch (IOException e) {
            System.err.println("Error loading image. Make sure your file is named IMG.png, or give a correct file name.");
            System.exit(1);
        }

        if (img == null) {
            System.err.println("Error loading image. Is the file in the correct format?");
            System.exit(5);
        }

        if (img.getHeight() != 124 && img.getWidth() != 128) {
            System.err.println("Image is not 124x128. image height must be 124, width must be 128.");
            System.exit(2);
        }

        for (int i = 0; i < 124; i++) {
            for (int j = 0; j < 128; j++) {

                Color pix = new Color(img.getRGB(j, i));

                // Scale each value down to the range 0 - 31
                int red = pix.getRed() / 8;
                int green = pix.getGreen() / 8;
                int blue = pix.getBlue() / 8;

                // Convert RGB to LC3 11111-11111-11111 style values
                int bColor = (red * 1024) + (green * 32) + blue;

                // Program does not create instructions for black pixels
                if (bColor == 0) {
                    continue;
                }

                String color = formatHex(Integer.toHexString(bColor));

                // Basically: xC000 + hex(iterationNumber), because program reads image location as one giant array,
                // instead of (x, y) to save space.
                String location = Integer.toHexString(49152 + ((i * 128) + j));

                byte[] byteColor = toByteArray(color);
                byte[] byteLocation = toByteArray(location);

                colorInstruction.add(byteColor);
                locationInstruction.add(byteLocation);
            }
        }
    }

    private static void writeData() {
        Random rand = new Random();

        if (colorInstruction.size() == 0) {
            System.out.println("Files will not be created for black images.");
            System.exit(0);
        }

        try {

            // Random three digit numbers for name?
            String objName = "drawImage_" + rand.nextInt(1000) + ".obj";

            File file = new File(objName);

            if (file.createNewFile()) {
                System.out.println(objName + " created...");
            } else {
                System.out.println(objName + " overwritten...");
            }

            OutputStream ostream = new FileOutputStream(file);

            // This is our program data, this will be converted to bytes and added before our image data.
            String insData = "30002404e00ce20b12420e01" + formatHex(Integer.toHexString(colorInstruction.size()))
                    + "6600684077001021126114bf03f9f025";
            ostream.write(toByteArray(insData));

            for (byte[] bArr : colorInstruction) {
                ostream.write(bArr);
            }

            for (byte[] bArr : locationInstruction) {
                ostream.write(bArr);
            }

            ostream.flush();
            ostream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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