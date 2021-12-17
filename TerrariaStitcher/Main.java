import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    // TODO:
    // Avoid using memory so I dont have to 'section' the final image
    // Figure out how terraria names its screencaps, or loop from 0->n
    // GUI?
    // Error catching
    public static void main(String[] args) throws IOException {
        // {x, y}, each coordinate represents n in the equation 40 + (126 * n)
        int[][] sizeRange = {{0, 0}, {50, 14}, {0, 0}};
        // 0 - small, 1 - medium, 2 - large
        int worldsize = 1;
        String folderPath = "";
        // Fragment the final image into this many sections, should be a divisor of sizeRange[][0]
        int sections = 5;

        for (int section = 0; section < sections; section++) {
            BufferedImage world = new BufferedImage(sizeRange[worldsize][0] * (2032 / sections),
                                                    sizeRange[worldsize][1] * 2016, 1);
            Graphics gWorld = world.createGraphics();

            int xFragsInSect = sizeRange[worldsize][0] / sections;
            int yFragsInSect = sizeRange[worldsize][1];
            for (int i = 0; i < xFragsInSect; i++) {
                for (int j = 0; j < yFragsInSect; j++) {
                    // File name of fragment : xFragName-yFragName.png
                    String xFragName = String.valueOf(40 + (i + section * xFragsInSect) * 126);
                    String yFragName = String.valueOf(40 + j * 126);
                    BufferedImage frag = ImageIO.read(new File(folderPath + xFragName + "-" + yFragName + ".png"));
                    // Draw loaded fragment into world, overlapping previous fragment by 16 pixels on x and y
                    gWorld.drawImage(frag, i * 2016, j * 2016, null);
                }
            }

            File file = new File(folderPath + "world" + String.valueOf(section) + ".png");
            if (file.createNewFile()) {
                System.out.println("Stitching (" + String.valueOf(section+1) + "/" + String.valueOf(sections) + ")");
            } else {
                System.out.println("Overwriting! (" + String.valueOf(section+1) + "/" + String.valueOf(sections) + ")");
            }
            ImageIO.write(world, "png", file);
        }
        System.out.println("Done");
    }
}
