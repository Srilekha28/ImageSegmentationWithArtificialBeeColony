package edu.uw.bothell.css.dsl.mass.apps.abc;

import edu.uw.bothell.css.dsl.MASS.*;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.GrayScaleIntensityConverter;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.HistogramPlotHelper;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.LineGraphHelper;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.TIFFImageReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtificialBeeColony extends Application {

    public static final List<List<Double>> AbandonedSolutions = new ArrayList<>();

    // We are trying to initialize 20 bees, 10- employee 10-onlooker
    public static final int N = 20;

    // we will try segmenting image into 7 classes, each class need 3 variables. So 9 dimensions
    public static final int D = 9;

    // iterations
    public static final int I = 1000;

    // bit in pixel
    public static final int PIXELS = 16;

    public static final int INTENSITY_MAX = (int) Math.pow(2, PIXELS);

    public static final double[] fitnessScore = new double[N];
    public static final float[] abandonedCount = new float[N];
    public static final int AbandonedSolutionLimit = 10000;
    public static void main(final String[] args) throws IOException {

        GrayScaleIntensityConverter imageConverter = new GrayScaleIntensityConverter();
        // test image path
        int w = 1022;
        int h = 1020;
        //String path = "/Users/dim/Downloads/lena.png";
//        BufferedImage grayScaleImage = imageConverter.convert(path, w, h, false);

        // "the_camera_man" image for ABC testing
//        int w = 487;
//        int h = 490;
//        String path = "/Users/dim/Downloads/The-original-cameraman-image.png";
//        BufferedImage grayScaleImage = imageConverter.convert(path, w, h, false);

        // histogram plot helper
        HistogramPlotHelper histPlotHelper = new HistogramPlotHelper();
        double[] hist;
//        TIFFImageReader tiffImageHelper = new TIFFImageReader();
        if (PIXELS == 8)
        {
//            hist = histPlotHelper.getHistPlotData(grayScaleImage, w, h); //  sized array with frequencies 1D
        } else
        {
            hist = TIFFImageReader.read(true);
            // if your image is not really 16 bit then use it, NOT APPLICABLE FOR ANIME
            //extrapolate(hist);
        }

        long start = Instant.now().toEpochMilli();
        // Step 1: Start with initializing an array of size N, dimension D
        //double[] expectedOutput = [0.307, 25.30, 32.01, 0.201, 9.80, 82.30, 0.249, 17.71, 127.00, 0.555, 17.21, 166.10];
        Initialization init = new Initialization();
        double[][] initPopulation = init.getPopulationInitialization();
        FitnessScoreHelper fitnessScoreHelper = new FitnessScoreHelper(hist);

        List<EmployeeBee> employeeBees = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            employeeBees.add(new EmployeeBee(fitnessScoreHelper));
        }

        List<OnlookerBee> onlookerBees = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            onlookerBees.add(new OnlookerBee(fitnessScoreHelper));
        }

        // Intermediate steps
        // initialize fitness score array and abandoned solution count
        for (int i = 0; i < N; i++)
        {
            fitnessScore[i] = FitnessScoreHelper.getFitnessValue(initPopulation[i]);
            abandonedCount[i] = 0;
        }
//        System.out.print("iteration : ");
        // ******** ABC STARTING **************
        for (int c = 0; c < I; c++) {
            // System.out.println(c + "..");
            // Step2: Initialize employee bess equal to food sources (N) and get next nearest best food source(solution)
            int count = 0;
            for (EmployeeBee employeeBee : employeeBees) {
                employeeBee.setNextBestSolution(initPopulation, count);
                count += 1;
            }

            for (OnlookerBee onlookerBee : onlookerBees) {
                onlookerBee.setNextBestSolution(initPopulation);
            }
        }

        int bestSolution = 0;
        double minVal = 0;
        for (int k = 0; k < N; k++)
        {
            if (fitnessScore[k] > minVal)
            {
                bestSolution = k;
                minVal = fitnessScore[k];
            }
        }

        int index = -1;
        for (int i = 0; i < AbandonedSolutions.size(); i++)
        {
            if (minVal < AbandonedSolutions.get(i).get(0))
            {
                index = i;
                minVal = AbandonedSolutions.get(i).get(0);
            }
        }

        double[] solution = initPopulation[bestSolution];
        if (index != -1)
        {
            System.out.println("choosing from abandoned solutions");
            for (int i = 0; i < D; i++)
            {
                solution[i] = AbandonedSolutions.get(index).get(i+1);
            }
        }

        System.out.println("final fitness score: " + minVal);
        System.out.println("Best solution: " + Arrays.toString(solution));
        double[][] histApprox = fitnessScoreHelper.getGaussianApproximations(solution);

//        System.out.println(Arrays.deepToString(histApprox));
        setData(hist, hist);
        System.out.println("histogram - " + Arrays.toString(hist));

        double[] finalCombine = new double[INTENSITY_MAX];
        for (int i = 0; i < INTENSITY_MAX; i ++)
        {
            finalCombine[i] = histApprox[0][i];
            for (int j = 1; j < D/3; j++)
            {
                finalCombine[i] += histApprox[j][i];
            }
//            finalCombine[i] = histApprox[0][i] + histApprox[1][i] + histApprox[2][i];
        }

        System.out.println("Abandoned solution count: " + Bee.abandonedSolutions);

        QuadraticEquationGeneration bestSol = new QuadraticEquationGeneration();
        double[] thresholds = bestSol.calculateThreshold(solution);
        //use this loop only if the image is not a real 16 bit image
        /*for (int g = 0; g < thresholds.length; g++)
        {
            thresholds[g] = thresholds[g]/256;
        }*/
        System.out.println(Arrays.toString(thresholds));
        long end = Instant.now().toEpochMilli();
        System.out.println("TOTAL TIME TAKEN: " + (end - start));

        if (PIXELS == 8) {
//            imageConverter.convertHelperRevert(grayScaleImage, w, h, thresholds);
//            File inputFile = new File(path);
//            String[] pathArgs = inputFile.getName().split("\\.");
//            String outputFilePath = pathArgs[0] + "_segmented_output.png";
//            File outputFile = new File(outputFilePath);
//            ImageIO.write(grayScaleImage, "png", outputFile);
        } else
        {
            TIFFImageReader.outputNewSegmentedImage(thresholds);
        }
    }

    @Override public void start(Stage stage) {
        stage.setTitle("Histogram Details of Camera Man Image");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Pixel Intensity");

        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Histogram");
        Scene scene  = new Scene(lineChart,800,600);

        XYChart.Series<Number, Number> series_originalHist = new XYChart.Series<>();
        series_originalHist.setName("Original Image Histogram");
        for (int i = 0; i < INTENSITY_MAX; i++)
        {
            series_originalHist.getData().add(new XYChart.Data<>(i, histogram[i]));
            if (i % 1000 == 0) System.out.println(i);
        }
        lineChart.getData().add(series_originalHist);


//        XYChart.Series<Number, Number> series_finalHist = new XYChart.Series<>();
//        series_finalHist.setName("Computed final Image Histogram");
//        for (int i = 0; i < INTENSITY_MAX; i++)
//        {
//            series_finalHist.getData().add(new XYChart.Data<>(i, finalhist[i]));
//            if (i % 1000 == 0) System.out.println(i);
//        }
//        lineChart.getData().add(series_finalHist);

        stage.setScene(scene);
        stage.show();
        System.out.println("DONEEE!!!!");
    }

    public static void setData(double[] histogram, double[] finalhist)
    {
        ArtificialBeeColony.histogram = histogram;
        ArtificialBeeColony.finalhist = finalhist;
    }

    /*public static void extrapolate(double[] hist)
    {
        int i = (int) (Math.pow(2,8) - 1);
        int j = (int) (Math.pow(2, 16) - 1);
        while (i >= 0)
        {
            for (int k = 0 ; k < 256; k++)
            {
                hist[j-k] = hist[i];
            }
            i--;
            j -= 256;
        }
    }*/

    public static double[] histogram;
    public static double[] finalhist;
}
