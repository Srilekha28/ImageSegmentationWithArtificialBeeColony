package edu.uw.bothell.css.dsl.mass.apps.abc;

import java.util.Arrays;
import java.util.Random;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.N;

public class OnlookerBee extends Bee {
    private final FitnessScoreHelper fitnessScoreHelper;
    OnlookerBee(FitnessScoreHelper fitnessScoreHelper){
        this.fitnessScoreHelper = fitnessScoreHelper;
    }

    Random random = new Random(23456);
    public int selectFoodSource()
    {
        // here we will use fitness scores and select food source
        double[] probs = new double[N];
        int range = 10000000;
        float sum = 0;
        for (int i = 0; i < N; i++)
        {
            sum += ArtificialBeeColony.fitnessScore[i];
        }

        probs[0] = (ArtificialBeeColony.fitnessScore[0] / sum) * range;
        for (int i = 1; i < N; i++)
        {
            probs[i] = probs[i - 1] + (ArtificialBeeColony.fitnessScore[i] / sum) * range;
        }

        int rand = random.nextInt(range);

        for (int i = 0; i < N; i++)
        {
            if (rand <= probs[i])
            {
                return i;
            }
        }

        throw new IllegalStateException("Not able to select: " + range + Arrays.toString(probs));
    }

    public void setNextBestSolution(double[][] population)
    {
        super.setNextBestSolution(population, selectFoodSource(), fitnessScoreHelper);
    }
}
