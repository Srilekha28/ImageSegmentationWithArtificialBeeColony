
package edu.uw.bothell.css.dsl.mass.apps.abc;
import java.util.*;
import java.util.Random;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.*;

/**
 * This class is used to initialize N(p) food sources with each food source being D-dimension
 */
public class Initialization {
    static Random random = new Random(2);
    int i = 0;
    int j = 0;
    int col = D;
    static int meanMinimum = 0;
    static int meanMaximum = INTENSITY_MAX - 1;
    static int stdMinimum = 1;
    static int stdMaximum = 200;

    double[][] populationInitialization;

    /**
     * Implementation of eq 4 in https://arxiv.org/pdf/1405.7229.pdf
     */
    Initialization()
    {
        populationInitialization = new double[N][col];
        // P(0-1), standard deviation(0-1), mean (0 - 255)
        // dimension = 9, numFoodSources - 20
        // [p1, s1, m1, p2, s2, m2, p3, s3, m3]
        for(i = 0; i < N ; i++){
            populationInitialization[i] = generateNewStart();
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

    public double[][] getPopulationInitialization()
    {
        return this.populationInitialization;
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
}
