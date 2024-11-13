package com.tot.utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author karthik on 12/06/22.
 * @project totservices
 */

public class TestImageResize {

    public static void main(String[] args) throws Exception {
        String path1 = "/Users/karthik/Downloads/me2.jpeg";
        String path2 = "/Users/karthik/Downloads/me2.jpeg";
        String out = "/Users/karthik/Downloads/";

        mergeImages(path1, path2, out);


        System.exit(0);
        String inPath = "/Users/karthik/Downloads/me2.jpeg";
        String outPath = "/Users/karthik/Downloads/me2_tiny100.jpeg";

        File file = new File(inPath);

        BufferedImage orig = null;
        try {
            orig = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage tinyImage = scale(orig, 100, 100);
        ImageIO.write(tinyImage, "jpeg", new File(outPath));

    }


    private static BufferedImage scale(BufferedImage source, int width, int height) {
        //        int w = (int) (source.getWidth() * ratio);
        //        int h = (int) (source.getHeight() * ratio);
        int w = width;
        int h = height;
        BufferedImage bi = getCompatibleImage(w, h);
        Graphics2D g2d = bi.createGraphics();
        double xScale = (double) w / source.getWidth();
        double yScale = (double) h / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }

    private static BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        return image;
    }

    public static void mergeImages(String path1, String path2, String outPath){
        try {
            // Load the two images
            BufferedImage image1 = ImageIO.read(new File(path1));
            BufferedImage image2 = ImageIO.read(new File(path2));

            // Create a new image that is the width of the two input images combined
            int combinedWidth = image1.getWidth() + image2.getWidth();
            int maxHeight = Math.max(image1.getHeight(), image2.getHeight());
            BufferedImage combinedImage = new BufferedImage(combinedWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);

            // Draw the first image onto the combined image
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(image1, 0, 0, null);

            // Draw the second image next to the first image
            g2d.drawImage(image2, image1.getWidth(), 0, null);
            g2d.dispose();

            // Write the combined image to a file
            ImageIO.write(combinedImage, "png", new File(outPath + "combined.jpg"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
