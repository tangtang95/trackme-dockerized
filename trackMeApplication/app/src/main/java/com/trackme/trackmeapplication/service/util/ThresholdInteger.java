package com.trackme.trackmeapplication.service.util;

public final class ThresholdInteger {

    public final Integer max;
    public final Integer min;

    /**
     * Constructor.
     * Create a new threshold integer based on max and min (the bound are also included)
     * @param max the max integer
     * @param min the min integer
     */
    public ThresholdInteger(Integer max, Integer min) {
        if(max < min)
            throw new IllegalStateException("impossible to have max less than min");
        this.max = max;
        this.min = min;
    }

    /**
     * @param value the value to be checked
     * @return true if the value is inside the interval (bound included), false otherwise
     */
    public boolean contains(Integer value){
        return value <= max && value >= min;
    }

}
