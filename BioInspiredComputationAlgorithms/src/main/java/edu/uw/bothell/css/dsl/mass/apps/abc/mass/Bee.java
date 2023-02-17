package edu.uw.bothell.css.dsl.mass.apps.abc.mass;

import edu.uw.bothell.css.dsl.MASS.Agent;
import edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony;
import edu.uw.bothell.css.dsl.mass.apps.abc.FitnessScoreHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.AbandonedSolutionLimit;
import static edu.uw.bothell.css.dsl.mass.apps.abc.mass.ImageSegmentationWithABC.D;
import static edu.uw.bothell.css.dsl.mass.apps.abc.mass.ImageSegmentationWithABC.INTENSITY_MAX;


public class Bee extends Agent implements Serializable {
    // Bee will contain common methods for both employee and onlooker bees
    // Bee will contain methods for both employee and onlooker. With MASS
    public static final int _init = 0;
    public static final int _findNextSolution = 1;
    static Random random = new Random(2);
    static int meanMinimum = 0;
    static int meanMaximum = INTENSITY_MAX - 1;
    static int stdMinimum = 1;
    static int stdMaximum = 200;

    double[] threshold;
    FitnessScoreMASS fitnessScoreMASS;
    double fitnessScore;
    int abandonedCount = 0;

    public Bee()
    {
        super();
    }

    public Bee( Object arg ) {
    }

    public Object callMethod( int functionId, Object argument ) {
        System.out.println("AgentId: " + this.getAgentId());
        switch( functionId ) {
            case _init:   return init( argument );
            case _findNextSolution: return findNextSolution( argument );
        }
        return null;
    }

    public Object init(Object arg) {
        threshold = generateNewStart();
        // arg contains hist array
        double[] hist = (double[]) arg;

        System.out.println("Histogram length " + hist.length);
        System.out.println("Histogram first and last values" + hist[0] + ", " + hist[65535]);
        fitnessScoreMASS = new FitnessScoreMASS((double[]) arg);
        System.out.println("Threshold coming with size " + threshold.length);
        fitnessScore = fitnessScoreMASS.getFitnessValue(threshold);

        return threshold;
    }

    public Object findNextSolution(Object arg) {
        if (abandonedCount > AbandonedSolutionLimit)
        {
            // generate a new solution here

            return null;
        }
        else
        {
            // following prev solution is used to generate a new solution
            double[] prevSolution = (double[]) arg;
            System.out.println("Coming from prev iteration" + Arrays.toString(prevSolution));
            double[] newSolution = new double[D];
            for (int j = 0; j < D; j++)
            {
                float phi = getPhiRandomValue();
                /**
                 * Implementation of eq 5 in https://arxiv.org/pdf/1405.7229.pdf
                 */
                double temp = threshold[j] + phi * (threshold[j] - prevSolution[j]);

                // following checks are for adjusting parameter value if it is out of bounds
                if (temp < 0) {
                    if (j % 3 == 0) {
                        temp = 0.1f;
                    } else {
                        temp = threshold[j];
                    }
                }
                else
                {
                    boolean adjust = (j % 3 == 0 && temp > 1)
                            || (j % 3 == 1 && temp > stdMaximum)
                            || (j % 3 == 2 && temp > meanMaximum);
                    if (adjust)
                        temp = threshold[j];
                }

                newSolution[j] = temp;
            }

            double newFitnessScore = fitnessScoreMASS.getFitnessValue(newSolution);

            if (fitnessScore < newFitnessScore)
            {
                // pixel intensity value, this should be an integer
                threshold = newSolution;
                fitnessScore = newFitnessScore;

                // reset the abandoned count
                abandonedCount = 0;
            }
            else
            {
                // increment abandoned count
                abandonedCount += 1;
            }
            //System.out.println("New threshold are " + Arrays.toString(threshold));
            return threshold;
        }
    }

    public static double[] generateNewStart()
    {
        double[] start = new double[D];
        float[] probs = generateProbabilities(D / 3);

        for(int j = 0 ; j < D ; j++){
            if(j % 3 == 0){
                start[j] = probs[j / 3];
            }
            else if(j % 3 == 1){
                start[j] = getRandomNumberUsingNextInt(stdMinimum, stdMaximum);
            }
            else{
                start[j] = getRandomNumberUsingNextInt(meanMinimum, meanMaximum);
            }
        }
        return start;
    }

    public static float[] generateProbabilities(int n) {
        int range = 1000000;
        float[] probs = new float[n];
        int prev = 0;
        float probSum = 0;
        for (int i = 0; i < n-1; i++)
        {
            int curr = getRandomNumberUsingNextInt(prev, range);

            probs[i] = (float) (curr - prev) / range;
            probSum += probs[i];
            prev = curr;
        }
        probs[n-1] = 1- probSum;
        float s = 0;
        if (probs.length != D / 3) {
            throw new IllegalStateException("Probs length is not as expected. Actual: " + probs.length + " expected : " + D / 3);
        }
        for (int i =0; i < n; i++)
        {
            s += probs[i];
        }
        if (s != 1) {
            throw new IllegalStateException("probs sum is not 1");
        }
        return probs;
    }


    public static int getRandomNumberUsingNextInt(int min, int max) {
        try {
            return random.nextInt(max - min) + min;
        } catch (IllegalArgumentException e)
        {
            System.out.println(max - min);
            return 0;
        }
    }

    public float getPhiRandomValue()
    {
        // trial 1: Phi to be floating point number between -1 and 1
        return 2 * random.nextFloat() - 1;
        // trail 2: phi to be integer -1 or 0 or 1
//        return rand.nextInt(3) - 2;
    }


}



