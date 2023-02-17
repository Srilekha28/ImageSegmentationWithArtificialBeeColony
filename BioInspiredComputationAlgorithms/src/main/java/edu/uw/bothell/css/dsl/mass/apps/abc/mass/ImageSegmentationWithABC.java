package edu.uw.bothell.css.dsl.mass.apps.abc.mass;

import edu.uw.bothell.css.dsl.MASS.Agents;
import edu.uw.bothell.css.dsl.MASS.MASS;
import edu.uw.bothell.css.dsl.MASS.Places;
import edu.uw.bothell.css.dsl.MASS.logging.LogLevel;
import edu.uw.bothell.css.dsl.mass.apps.abc.FitnessScoreHelper;
import edu.uw.bothell.css.dsl.mass.apps.abc.QuadraticEquationGeneration;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.GrayScaleIntensityConverter;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.HistogramPlotHelper;
import edu.uw.bothell.css.dsl.mass.apps.abc.utils.TIFFImageReader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ImageSegmentationWithABC {
    // We are trying to initialize 20 bees, 10- employee 10-onlooker
    public static final int N = 20;

    // we will try segmenting image into 3 classes, each class need 3 variables. So 9 dimensions
    public static final int D = 21;

    // iterations
    public static final int I = 10000;

    // bit in pixel
    public static final int PIXELS = 16;

    public static final int INTENSITY_MAX = (int) Math.pow(2, PIXELS);

    public static final Random rand = new Random(200);
    public static final String NODES_PATH = "nodes.xml";
    public static void main(String[] args) throws IOException {
        GrayScaleIntensityConverter imageConverter = new GrayScaleIntensityConverter();
        // test image path
        int w = 1022;
        int h = 1020;
//        BufferedImage grayScaleImage = imageConverter.convert(path, w, h, false);
        BufferedImage grayScaleImage = null;
        HistogramPlotHelper histPlotHelper = new HistogramPlotHelper();
        double[] hist;
//        TIFFImageReader tiffImageHelper = new TIFFImageReader();
        if (PIXELS == 8)
        {
            hist = histPlotHelper.getHistPlotData(grayScaleImage, w, h); //  sized array with frequencies 1D
        } else
        {
            hist = TIFFImageReader.read(true);
            // if your image is not really 16 bit, then use extrapolate function NOT FOR ANIME
            //extrapolate(hist);
        }

        FitnessScoreHelper fitnessScoreHelper = new FitnessScoreHelper(hist);

        // Read and validate input parameters
        if ( args.length != 4 ) {
            System.err.println( "args = " + args.length + " should be 6:" +
                    " size, nAgents, iteration, show[y/n]" );
            System.exit( -1 );
        }
        int size = Integer.parseInt( args[0] ); // 5
        int nAgents = Integer.parseInt( args[1] ); // 25
        int iteration = Integer.parseInt( args[2] ); // 100
//        int nProc = Integer.parseInt( args[3] );
//        int nThr = Integer.parseInt( args[4] );
        boolean show = args[3].equals( "y" );
        if ( size < 1 || nAgents > size * size || iteration <= 0 ) {
            System.err.println( "size(" + size + ") should be > 0" );
            System.err.println( "nAgents(" + nAgents + ") should be < size(" + size + ")" );
            System.err.println( "iteration(" + iteration + " should be > 0 " );
            System.exit( -1 );
        }
        System.out.println( "size = " + size + " nAgents = " + nAgents );
        MASS.setLoggingLevel( LogLevel.DEBUG );
        MASS.setNodeFilePath(NODES_PATH);
        MASS.init();

        // Creating places and calling init method to populate initial threshold values
        Places imageThresholds = new Places( 1, ImageThreshold.class.getName( ), null, size, size );
        System.out.println("going to initialize places now... ");
        imageThresholds.callAll( ImageThreshold.init_ );
        System.out.println("place initialization DONE... ");

        // Time measurement starts
        System.out.println( "Go!" );
        long startTime = System.currentTimeMillis( );

        // Instantiate an agent at each node
        Agents bees = new Agents( 2, Bee.class.getName( ), null, imageThresholds, nAgents );

        double[][] prevIterationSolutions = new double[nAgents][];
        double[][] toBePassedNextIteration = new double[nAgents][];
        // change the following boolean to switch from callAll to doAll
        boolean callAll = true;

        double[][] histCopies = new double[nAgents][];
        for (int ind = 0; ind < nAgents; ind++)
        {
            histCopies[ind] = hist;
        }
        if (show)
            System.out.println("... step 1: Started ABC implementation for iteration.................");
        try {
            for (int i = 0; i < iteration; i++) {
                Object[] retSolutions;
                if (i == 0) {
                    // init agent
                    if (callAll)
                        retSolutions = (Object[]) bees.callAll(Bee._init, histCopies);
                    else
                        retSolutions = (Object[]) bees.doAll(Bee._init, histCopies, 1);
                } else {
                    if (callAll)
                        retSolutions = (Object[]) bees.callAll(Bee._findNextSolution, toBePassedNextIteration);
                    else
                        retSolutions = (Object[]) bees.doAll(Bee._findNextSolution, toBePassedNextIteration, 1);
                }
                if (callAll) bees.manageAll();
                int j = 0;
                for (Object o : retSolutions) {
                    prevIterationSolutions[j] = (double[]) o;
                    j++;
                }
                int k = getK(nAgents);
                //System.out.println("Randomly generated k value " + k);
                for (j = 0; j < nAgents; j++) {
                    if (j != k)
                        toBePassedNextIteration[j] = prevIterationSolutions[k];
                    else
                    {
                        int newK = getK(nAgents);
                        //System.out.println("For matching agent, regenerating K value: "+ newK);
                        toBePassedNextIteration[j] = prevIterationSolutions[newK];
                    }

                }
                //System.out.println("To be passed in next iteration" + Arrays.toString(toBePassedNextIteration[0]));
            }

            // Time measurement starts
            System.out.println("Total time taken for MASS iterations: " + (System.currentTimeMillis( ) - startTime));
            System.out.println("***************** MASS iterations are done *******************");

            // using prevItenrations find best one
            double[] bestSolution = null;
            double fitnessScore = 0;
            for (int s = 0; s < nAgents; s++) {
                double temp = FitnessScoreHelper.getFitnessValue(prevIterationSolutions[s]);
                // System.out.println("Current solution fitness score: " + temp);
                if (temp > fitnessScore) {
                    bestSolution = prevIterationSolutions[s];
                    fitnessScore = temp;
                    // System.out.println("Found a better solution: " + Arrays.toString(bestSolution));
                }
            }

            // TODO: use above bestSolution and find thresholds like in serial impl
            System.out.println("Best solution: " + Arrays.toString(bestSolution));
            double[][] histApprox = fitnessScoreHelper.getGaussianApproximations(bestSolution);
            double[] finalCombine = new double[INTENSITY_MAX];
            for (int i = 0; i < INTENSITY_MAX; i ++)
            {
                finalCombine[i] = histApprox[0][i] + histApprox[1][i] + histApprox[2][i];
            }

            //System.out.println("Abandoned solution count: " + edu.uw.bothell.css.dsl.mass.apps.abc.Bee.abandonedSolutions);

            // new fixed solution
            QuadraticEquationGeneration bestSol = new QuadraticEquationGeneration();
            double[] thresholds = bestSol.calculateThreshold(bestSolution);
            System.out.println(Arrays.toString(thresholds));
            //long end = Instant.now().toEpochMilli();
            //System.out.println("TOTAL TIME TAKEN: " + (end - start));

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

        } finally {
            MASS.finish();
        }
        //System.out.println(Arrays.deepToString(histApprox));


    }

    public static int getK(int agents)
    {
        return rand.nextInt(agents);
    }

    public static void extrapolate(double[] hist)
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
    }
}
