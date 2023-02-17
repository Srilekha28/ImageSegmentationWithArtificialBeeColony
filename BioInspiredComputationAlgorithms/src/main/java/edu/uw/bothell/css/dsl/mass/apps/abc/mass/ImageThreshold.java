package edu.uw.bothell.css.dsl.mass.apps.abc.mass;

import edu.uw.bothell.css.dsl.MASS.Place;

import java.util.Arrays;

import static edu.uw.bothell.css.dsl.mass.apps.abc.ArtificialBeeColony.D;

public class ImageThreshold extends Place {
    /**
     * Image threshold will contain an array of threshold values
     * which will be updated by agents if there is a better threshold is found
     */

    // function identifiers
    public static final int init_ = 0;

    public ImageThreshold( ) {
        super( );
    }

    public ImageThreshold( Object arg ) {
    }

    @Override
    public Object callMethod( int functionId, Object argument ) {
        switch( functionId ) {
            case init_: return init(  );
        }
        return null;
    }
//
//    public double[] prevBeeSolution = new double[D];

    public Object init( ) {
        // [TODO 1] we will initialize threshold array with some thresholds
        System.err.println("Did come here .. " + Arrays.toString(this.getIndex()));
        return null;
    }
}
