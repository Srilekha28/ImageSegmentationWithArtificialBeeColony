package edu.uw.bothell.css.dsl.mass.apps.abc;

import edu.uw.bothell.css.dsl.MASS.*;

import java.io.Serializable;
import java.util.Random;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.*;

public class EmployeeBee extends Bee {
    private final FitnessScoreHelper fitnessScoreHelper;

    EmployeeBee(FitnessScoreHelper fitnessScoreHelper){
        this.fitnessScoreHelper = fitnessScoreHelper;
    }

    public void setNextBestSolution(double[][] population, int row)
    {
        super.setNextBestSolution(population, row, fitnessScoreHelper);
    }
}
