package edu.uw.bothell.css.dsl.mass.apps.abc.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.D;

/**
 * This utility class contains methods to convert an image into gray scale intensity matrix.
 * Gray scale image contains intensities between 0..255 (only integers)
 * It also contains methods to find intensity frequencies for each intensity level.
 * intensity frequencies (can be used plotting histograms)
 */
public class GrayScaleIntensityConverter {

    public BufferedImage convert(String imagePath, int width, int height, boolean writeToFile)
    {
        try
        {
            File inputFile = new File(imagePath);
            BufferedImage image = ImageIO.read(inputFile);
            convertHelper(image, width, height);
            if (writeToFile) {
                String[] args = inputFile.getName().split("\\.");
                String outputFilePath = args[0] + "_output.png";
                File outputFile = new File(outputFilePath);
                ImageIO.write(image, "png", outputFile);
            }
            return image;
        } catch (IOException e) {
            final String errorMsg = "Exception during reading image data: imagePath " + imagePath;
            System.out.print(errorMsg);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * For every pixel in the image we will fetch RGB value of it.
     * From RGB value, will use following formula to convert RGB value to gray scale value.
     * This formula is also used in PAL/NTSC, learn more about this in https://en.m.wikipedia.org/wiki/Grayscale
     *
     * Formula = 0.299R'+0.587G'+0.114B'
     *
     * @param bufImage
     * @param ImgWidth
     * @param ImgHeight
     */
    private void convertHelper(BufferedImage bufImage, int ImgWidth, int ImgHeight) {
        for (int w = 0; w < ImgWidth; w++) {
            for (int h = 0; h < ImgHeight; h++) {

                Color color = new Color(bufImage.getRGB(w, h));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                // avg intensity value
                double intensity = 0.299*r + 0.587*g + 0.114*b;

                // make sure that intensity is between 0..255
                int finalIntensity;
                if (intensity > 255)
                    finalIntensity = 255;
                else if (intensity < 0)
                    finalIntensity = 0;
                else
                    finalIntensity = (int) Math.floor(intensity);

                // new color with all values being same
                Color avg = new Color(finalIntensity, finalIntensity, finalIntensity);

                bufImage.setRGB(w, h, avg.getRGB());
            }
        }
    }

    public void convertHelperRevert(BufferedImage bufImage, int ImgWidth, int ImgHeight, double[] thresholds) {
        Arrays.sort(thresholds);
        int l = thresholds.length;
        if (l != ((D/3)-1)){
            throw new IllegalStateException("thresholds count is not same as number of classes");
        }

        for (int w = 0; w < ImgWidth; w++) {
            for (int h = 0; h < ImgHeight; h++) {
                Color color = new Color(bufImage.getRGB(w, h));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                double intensity = r;
                int finalIntensity = 255;

                int gap = 256 / l;
                for (int i = 0; i < l; i++) {
                    if (intensity <= thresholds[i]) {
                        finalIntensity = Math.min(gap * i, 255);
                        break;
                    }
                }

                // threshold value
//                double t1 = thresholds[0] < thresholds[1] ? thresholds[0] : thresholds[1];
//                double t2 = thresholds[0] >= thresholds[1] ? thresholds[0] : thresholds[1];


//                if (intensity <= t1){
//                    finalIntensity = 0;
//                } else if(intensity <= t2){
//                    finalIntensity = 125;
//                }else{
//                    finalIntensity = 255;
//                }

                // new color with all values being same
                Color avg = new Color(finalIntensity, finalIntensity, finalIntensity);

                bufImage.setRGB(w, h, avg.getRGB());
            }
        }
    }
}
