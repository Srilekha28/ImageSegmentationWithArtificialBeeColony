package edu.uw.bothell.css.dsl.mass.apps.abc.utils;

import com.sun.media.jai.codec.*;

import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.*;

public class TIFFImageReader {
    static ColorModel cm;
    static WritableRaster wRaster;
    static Raster raster;
    static int w;
    static int h;

    private static final String name = "brain_508";


    public static void main(String[] args) throws IOException {
        double[] thres = new double[]{145218.92963335983, 87475.10446300251, 52427.23630106298, 41579.57430743024, 2673406.998969571, 26778.05588700255};
        read(false);
        outputNewSegmentedImage(thres);
    }

    public static double[] read(boolean writeOutput) throws IOException {
        String filePath = "/Users/dim/Downloads/" + name + ".tif";
        File file = new File(filePath);

        SeekableStream s = new FileSeekableStream(file);
        TIFFDecodeParam param = null;

        /*
         * ImageDecoder is from com.sun.media.jai.codec.ImageDecoder package
         */
        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
        raster = dec.decodeAsRaster();

        w = raster.getWidth();
        h = raster.getHeight();
        // int averagePixel[][][] = new int[w][h][3];
        int histSize = (int) (Math.pow(2, 16));
        int[] hist = new int[histSize];
        for (int i = 0; i < histSize; i++)
        {
            hist[i] = 0;
        }

        int tot = 0;

        cm = dec.decodeAsRenderedImage().getColorModel();
        wRaster = dec.decodeAsRenderedImage().getData().createCompatibleWritableRaster();

        for (int width = 0; width < w; width++) {
            for (int height = 0; height < h; height++) {
                int[] pixel = null;

                pixel = raster.getPixel(width, height, pixel);
                int r = pixel[0], g = pixel[1], b = pixel[2];

                int intensity = (int) (0.299*r + 0.587*g + 0.114*b);

                if (intensity > histSize)
                {
                    intensity = histSize;
                }
                hist[intensity] += 1;
                tot += 1;

                wRaster.setPixel(width, height, new int[]{intensity, intensity, intensity});
            }
        }


        for (int width = 0; width < w; width++) {
            for (int height = 0; height < h; height++) {


            }
        }

        double[] histFrequencies = new double[histSize];
        for (int i = 0; i < histSize; i++)
        {
            histFrequencies[i] = (double) hist[i] / tot;
        }

        if (writeOutput) {
            File outputFile = new File("/Users/dim/Downloads/" + name + "_grayscale_"
                    + System.currentTimeMillis() + ".tif");
            FileOutputStream fileoutput = new FileOutputStream(outputFile);
            TIFFEncodeParam encParam = null;
            ImageEncoder enc = ImageCodec.createImageEncoder("tiff", fileoutput, encParam);
            enc.encode(wRaster, cm);
            fileoutput.close();
        }

        return histFrequencies;
    }

    public static void outputNewSegmentedImage(double[] thresholds) throws IOException {
        Arrays.sort(thresholds);
        int l = thresholds.length;
        if (l != ((D/3)-1)){
            throw new IllegalStateException("thresholds count is not same as number of classes");
        }

        for (int width = 0; width < w; width++) {
            for (int height = 0; height < h; height++) {
                int[] pixel = null;

                pixel = wRaster.getPixel(width, height, pixel);
                int intensity = pixel[0];
                int gap = INTENSITY_MAX / l;
                int finalIntensity = INTENSITY_MAX - 1;
                for (int i = 0; i < l; i++) {
                    if (intensity <= thresholds[i]) {
                        int temp = 0;
                        if (i != 0)
                            temp = (int) thresholds[i];
                        finalIntensity = Math.min(temp, finalIntensity);
                        break;
                    }
                }

                wRaster.setPixel(width, height, new int[]{finalIntensity, finalIntensity, finalIntensity});
            }
        }

        File outputFile = new File("/Users/dim/Downloads/" + name + "_SegmentedOutput_"
                + System.currentTimeMillis() + ".tif");
        FileOutputStream fileoutput = new FileOutputStream(outputFile);
        TIFFEncodeParam encParam = null;
        ImageEncoder enc = ImageCodec.createImageEncoder("tiff", fileoutput, encParam);
        enc.encode(wRaster, cm);
        fileoutput.close();
    }

}
