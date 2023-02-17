package edu.uw.bothell.css.dsl.mass.apps.abc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.*;
import static edu.uw.bothell.css.dsl.mass.apps.abc.Initialization.*;

public class Bee {
    public static final Random rand = new Random(200);
    public static int betterSolution = 0;
    public static int worstSolution = 0;
    public static int abandonedSolutions = 0;
    private int k_value;

    private void setK()
    {
        k_value = getK();
    }

    public void setNextBestSolution(double[][] population, int row, FitnessScoreHelper fitnessScoreHelper)
    {
        // abandoned logic
        if (ArtificialBeeColony.abandonedCount[row] > AbandonedSolutionLimit)
        {
            // HERE employee or onlooker changes to scout and re-initialize the solution
            abandonedSolutions += 1;
            // in this case, we will try to find a nearest solution but we re-initiate this solution
            // more like resetting with random new solution, because solution might have stuck in local minima
            double[] newStart = Initialization.generateNewStart();
            ArtificialBeeColony.abandonedCount[row] = 0;
            // fitness score of this solution is not 0 but we need to re-calculate
            List<Double> adSol = new ArrayList<>();
            adSol.add(ArtificialBeeColony.fitnessScore[row]);
            for (int i = 0; i < D; i++)
            {
                adSol.add((double) newStart[i]);
            }
            AbandonedSolutions.add(adSol);

            population[row] = newStart;
            ArtificialBeeColony.fitnessScore[row] = fitnessScoreHelper.getFitnessValue(population[row]);

            return;
        }

        // this is to make sure that k != i
        // one unknown at this point is that should k be same for all rows or k should be randomly selected for all rows
        // Change we made is to make k same for all rows expect for one row where i == k
        setK();
        int k = k_value;
        while (k == row)
        {
            k = getK();
        }

        double[] newSolution = new double[D];
        for (int j = 0; j < D; j++)
        {
            float phi = getPhiRandomValue();
            /**
             * Implementation of eq 5 in https://arxiv.org/pdf/1405.7229.pdf
             */
            double temp = population[row][j] + phi * (population[row][j] - population[k][j]);

            // following checks are for adjusting parameter value if it is out of bounds
            if (temp < 0) {
                if (j % 3 == 0) {
                    temp = 0.1f;
                } else {
                    temp = population[row][j];
                }
            }
            else
            {
                boolean adjust = (j % 3 == 0 && temp > 1)
                        || (j % 3 == 1 && temp > stdMaximum)
                        || (j % 3 == 2 && temp > meanMaximum);
                if (adjust)
                    temp = population[row][j];
            }

            newSolution[j] = temp;
        }

        // reset probs if required
        boolean resetProbs = false;
        if (resetProbs)
        {
            float[] probs = generateProbabilities(D / 3);
            for (int j = 0; j < D; j+= D/3)
            {
                newSolution[j] = probs[j/3];
            }
        }

        double fitnessScore = FitnessScoreHelper.getFitnessValue(newSolution);

        if (ArtificialBeeColony.fitnessScore[row] < fitnessScore)
        {
            // pixel intensity value, this should be an integer
            population[row] = newSolution;
            ArtificialBeeColony.fitnessScore[row] = fitnessScore;

            // reset the abandoned count
            ArtificialBeeColony.abandonedCount[row] = 0;
            Bee.betterSolution += 1;
        }
        else
        {
            // increment abandoned count
            ArtificialBeeColony.abandonedCount[row] += 1;
            worstSolution += 1;
        }
    }

    public float getPhiRandomValue()
    {
        // trial 1: Phi to be floating point number between -1 and 1
        return 2 * rand.nextFloat() - 1;
        // trail 2: phi to be integer -1 or 0 or 1
//        return rand.nextInt(3) - 2;
    }

    public int getK()
    {
        return rand.nextInt(N);
    }
}
