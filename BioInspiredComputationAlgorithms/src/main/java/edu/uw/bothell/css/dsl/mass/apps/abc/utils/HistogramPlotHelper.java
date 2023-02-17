package edu.uw.bothell.css.dsl.mass.apps.abc.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogramPlotHelper {
    public double[] getHistPlotData(final BufferedImage image, int width, int height)
    {
        double[] hist = new double[256];
        for (int i = 0; i < 256; i++)
        {
            hist[i] = 0.0F;
        }
        int N = width * height;

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                Color color = new Color(image.getRGB(w, h));
                // r,g,b values will be same because image should have been converted to gray scale
                hist[color.getRed()]++;
            }
        }

        for (int i = 0; i < 256; i++)
        {
            hist[i] = hist[i] / N;
        }

        return hist;
    }
}
