package edu.uw.bothell.css.dsl.mass.apps.abc.mass;

import static edu.uw.bothell.css.dsl.mass.apps.abc.mass.ImageSegmentationWithABC.D;
import static edu.uw.bothell.css.dsl.mass.apps.abc.mass.ImageSegmentationWithABC.PIXELS;

public class FitnessScoreMASS {

    // ω being the penalty associated with the constrain of Pi sum equal to 1
    private static final float penalty = (float) Math.pow(10, -3);
    private static final int intensityMax = (int) Math.pow(2, PIXELS);

    private final double[] hist;
    public FitnessScoreMASS(double[] hist)
    {
        this.hist = hist;
    }

    public double getFitnessValue(double[] newSolution)
    {
        double J = getObjectiveFunctionValue(newSolution);
        double fitness;
        if (J > 0)
            fitness = 1 / (1+J);
        else
            fitness = 1 + Math.abs(J);

        return fitness;
    }

    private double getObjectiveFunctionValue(double[] newSolution)
    {
        double objectiveFuncValue = 0;

        for (int i = 0; i < intensityMax; i++)
        {
            double actual = calculateGaussianProbabilityForAllClasses(newSolution, i);
            double expected = hist[i];

            objectiveFuncValue += Math.pow(actual - expected, 2);
//            objectiveFuncValue += 100* Math.abs(actual - expected);
        }
        objectiveFuncValue /= intensityMax;

        float probSum = 0;
        for (int i = 0; i < D / 3; i++)
        {
            probSum += newSolution[3*i];
        }
        double probabilityPenalty = penalty * Math.abs(probSum - 1);
//        System.out.println("objectiveFuncValue: " + objectiveFuncValue);
        return objectiveFuncValue + probabilityPenalty;
    }

    private double calculateGaussianProbabilityForAllClasses(double[] newSolution, int intensity)
    {
        double tot = 0;
        for (int k = 0; k < D; k += 3)
        {
            tot += calculateGaussianProbability(newSolution[k], newSolution[k + 1], newSolution[k + 2], intensity);
        }
        return tot;
    }

    /**
     * sigma - standard deviation
     * mu - mean
     * p - probability
     * @param p
     * @param sigma
     * @param mu
     * @return
     */
    private double calculateGaussianProbability(double p, double sigma, double mu, int intensity)
    {
        if (p < 0 || p > 1 || sigma < 0)
        {
            throw new IllegalStateException("Probabilities or Variance not in expected range");
        }
        double sqrtOfTwoPiSigma = (float) Math.sqrt(2 * 3.14 * sigma);
        double exp = (double) Math.exp(-1 * Math.pow(intensity - mu, 2) / (2 * Math.pow(sigma, 2)));

        return (p / sqrtOfTwoPiSigma) * exp;
    }

    /**
     * Function get gaussian values at the end with a final solution
     * @param newSolution
     * @return
     */
    public double[][] getGaussianApproximations(double[] newSolution)
    {
        double[][] histSolutionValues = new double[D/3][intensityMax];
        for (int k = 0; k < D; k += 3)
        {
            for (int j = 0; j < intensityMax; j++)
            {
                histSolutionValues[k / 3][j] = calculateGaussianProbability(newSolution[k], newSolution[k + 1], newSolution[k + 2], j);
            }
        }
        return histSolutionValues;
    }
}
