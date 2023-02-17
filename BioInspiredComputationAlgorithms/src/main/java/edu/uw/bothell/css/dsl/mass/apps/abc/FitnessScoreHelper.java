package edu.uw.bothell.css.dsl.mass.apps.abc;

import java.io.Serializable;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.D;
import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.PIXELS;

public class FitnessScoreHelper implements Serializable {

//    public static void main(String[] args){
//        //0.087266, 5.0, 71.0, 0.16991, 4.0, 165.0, 0.615052, 12.0, 179.0
//        double[] finalSolutionObtained = new float[]{0.956044f, 28.0f, 105.0f, 0.03879f, 26.0f, 121.0f, 0.002625f, 13.0f, 0.0f};
//        hist = new float[] {0.0042660185F, 0.0F, 0.0F, 0.0F, 0.0025562586F, 0.0F, 0.0050580394F, 0.0F, 0.02280937F, 0.0F, 0.029137159F, 0.0F, 0.045937225F, 0.0F, 0.06485354F, 0.0F, 0.0F, 0.0F, 0.0075304867F, 0.0F, 0.009914931F, 0.0F, 0.0F, 0.0F, 0.0074508656F, 0.0F, 0.0F, 0.0F, 0.0031848468F, 0.0F, 0.0059673972F, 0.0F, 0.0F, 0.0F, 0.002958555F, 0.0F, 0.0027196915F, 0.0F, 0.0026777857F, 0.0F, 0.0027322634F, 0.0F, 0.004827557F, 0.0F, 0.0F, 0.0F, 0.0022629176F, 0.0F, 0.0022712986F, 0.0F, 0.003863722F, 0.0F, 0.0F, 0.0F, 0.002095294F, 0.0F, 0.0021288188F, 0.0F, 0.0021036752F, 0.0F, 0.0023425387F, 0.0F, 0.0022210116F, 0.0F, 0.0023509199F, 0.0F, 0.002003101F, 0.0F, 0.0019276704F, 0.0F, 0.0016427105F, 0.0F, 0.0016929975F, 0.0F, 0.0016888069F, 0.0F, 0.0030758916F, 0.0F, 0.0F, 0.0F, 0.0016049952F, 0.0F, 0.0018522398F, 0.0F, 0.0019402422F, 0.0F, 0.0020198633F, 0.0F, 0.0023467292F, 0.0F, 0.0026568328F, 0.0F, 0.0028621715F, 0.0F, 0.0032979927F, 0.0F, 0.0037380045F, 0.0F, 0.004433642F, 0.0F, 0.004840129F, 0.0F, 0.005460336F, 0.0F, 0.0061266394F, 0.0F, 0.006528936F, 0.0F, 0.007316767F, 0.0F, 0.0077148722F, 0.0F, 0.008569753F, 0.0F, 0.008703851F, 0.0F, 0.009537778F, 0.0F, 0.009873025F, 0.0F, 0.009688639F, 0.0F, 0.010669237F, 0.0F, 0.011322969F, 0.0F, 0.011628881F, 0.0F, 0.0F, 0.018044671F, 0.0F, 0.012303567F, 0.0F, 0.012408331F, 0.0F, 0.011905461F, 0.0F, 0.011478021F, 0.0F, 0.0107656205F, 0.0F, 0.010681809F, 0.0F, 0.011197251F, 0.0F, 0.013527218F, 0.0F, 0.01475087F, 0.0F, 0.017059883F, 0.0F, 0.017918954F, 0.0F, 0.016686922F, 0.0F, 0.017881239F, 0.0F, 0.02139295F, 0.0F, 0.024531702F, 0.0F, 0.032045428F, 0.0F, 0.03763567F, 0.0F, 0.034450825F, 0.0F, 0.05639274F, 0.0F, 0.0F, 0.0F, 0.02159829F, 0.0F, 0.040372126F, 0.0F, 0.0F, 0.0F, 0.019444328F, 0.0F, 0.021040943F, 0.0F, 0.038938943F, 0.0F, 0.0F, 0.0F, 0.014285714F, 0.0F, 0.009081004F, 0.0F, 0.0047311736F, 0.0F, 0.0025059716F, 0.0F, 0.0031094162F, 0.0F, 0.0F, 0.0F, 0.0013619411F, 0.0F, 0.001190127F, 0.0F, 0.0016636634F, 0.0F, 0.0F, 0.0F, 7.207811E-4F, 0.0F, 0.0011943176F, 0.0F, 0.0F, 0.0F, 5.4058584E-4F, 0.0F, 5.6991994E-4F, 0.0F, 4.609647E-4F, 0.0F, 4.4420233E-4F, 0.0F, 3.771529E-4F, 0.0F, 5.8249175E-4F, 0.0F, 8.590705E-4F, 0.0F, 0.0F, 0.0F, 4.4420233E-4F, 0.0F, 4.7353643E-4F, 0.0F, 4.4001173E-4F, 0.0F, 7.878305E-4F, 0.0F, 0.0F, 0.0F, 3.6458115E-4F, 0.0F, 4.483929E-4F, 0.0F, 3.9391525E-4F, 0.0F, 3.436282E-4F, 0.0F, 3.2686585E-4F, 0.0F, 4.2743998E-4F, 0.0F, 8.925952E-4F, 0.0F, 0.0F, 0.0F, 7.920211E-4F, 0.0F, 0.002744835F};
//        double v = getFitnessValue(finalSolutionObtained);
//        System.out.println(v);
//    }


    // ω being the penalty associated with the constrain of Pi sum equal to 1
    private static final float penalty = (float) Math.pow(10, -3);
    private static final int intensityMax = (int) Math.pow(2, PIXELS);

    private static double[] hist;
    public FitnessScoreHelper(double[] hist)
    {
        this.hist = hist;
    }

    public static double getFitnessValue(double[] newSolution)
    {
        double J = getObjectiveFunctionValue(newSolution);
        double fitness;
        if (J > 0)
            fitness = 1 / (1+J);
        else
            fitness = 1 + Math.abs(J);

        return fitness;
    }

    private static double getObjectiveFunctionValue(double[] newSolution)
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

    private static double calculateGaussianProbabilityForAllClasses(double[] newSolution, int intensity)
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
    private static double calculateGaussianProbability(double p, double sigma, double mu, int intensity)
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
    public static double[][] getGaussianApproximations(double[] newSolution)
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
