package edu.uw.bothell.css.dsl.mass.apps.abc;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.D;

public class QuadraticEquationGeneration {
    int K = D / 3;
    double[] thresholds = new double[K-1];
    double [] BestSolution;
    double[] finalThresholds ;
    int constant = 0;

    public double[] calculateThreshold(double[] BestSolution){
        this.BestSolution = BestSolution;
        SortedMap<Double, Integer> stdMap = new TreeMap<>();
        for (int i = 0; i < K; i++)
        {
            stdMap.put(BestSolution[3*i + 1], i);
        }

        int[] order = new int[D / 3];
        AtomicInteger k = new AtomicInteger();

        stdMap.entrySet().stream().forEach(entry -> {
            order[k.getAndIncrement()] = entry.getValue();

        });
        double[] solution = new double[D];
        for (int i = 0; i < D / 3; i++)
        {
            solution[3*i] = BestSolution[3*order[i]];
            solution[3*i + 1] = BestSolution[3*order[i] + 1];
            solution[3*i + 2] = BestSolution[3*order[i] + 2];
        }

        System.out.println(stdMap);
        System.out.println(Arrays.toString(BestSolution));
        System.out.println(Arrays.toString(solution));
        int params = 3;
        for (int i = 0; i < D-params; i+=params){

            double a = ThreshHoldComputation.A_Computation(solution[i+1], solution[i+1+params]);
            double b = ThreshHoldComputation.B_Computation(solution[i+2],solution[i+2+params], solution[i+1], solution[i+1+params]);
            double c = ThreshHoldComputation.C_Computation(solution[i],solution[i+params], solution[i+1], solution[i+1], solution[i+2],solution[i+2+params]);
            double x = ThreshHoldComputation.x_Computation(a,b,c);
            // double equation = a * Math.pow(x,2) + b * x + c;
            thresholds[constant] = x;
            constant += 1;
        }
        return thresholds;
    }
}
