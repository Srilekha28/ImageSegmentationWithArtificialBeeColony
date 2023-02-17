package edu.uw.bothell.css.dsl.mass.apps.abc;

public class ThreshHoldComputation {

    public static double A_Computation(double deviation1, double deviation2){
        return Math.pow(deviation1, 2) - Math.pow(deviation2, 2);
    }

    public static double B_Computation(double mean1, double mean2, double deviation1, double deviation2){
        double firstPart = mean1 * Math.pow(deviation2,2);
        double secondPart = mean2 * Math.pow(deviation1,2);

        return 2 * (firstPart - secondPart);
    }

    public static double C_Computation(double probability1, double probability2, double deviation1, double deviation2, double mean1, double mean2){
        double firstPart = Math.pow(deviation1 * mean2, 2);
        double secondPart = Math.pow(deviation2 * mean1, 2);
        double deviationProduct = 2 * Math.pow(deviation1 * deviation2 , 2);
        double logPart = Math.log((deviation2 * probability1) / (deviation1 * probability2));

        return firstPart - secondPart + deviationProduct * logPart;
    }

    public static double x_Computation(double a, double b, double c){
        double b_Square = Math.pow(b,2);
        double sqaueRootComputation = Math.sqrt(b_Square - 4 * a * c);
        double negative_possibility = ((-(b) - sqaueRootComputation) / (2 * a));
        double positive_possibility = ((-(b) + sqaueRootComputation) / (2 * a));

        return Math.max(negative_possibility, positive_possibility);
    }
}
