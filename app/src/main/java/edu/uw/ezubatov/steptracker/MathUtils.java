package edu.uw.ezubatov.steptracker;

/**
 * Created by jonf on 3/26/2018.
 */

public final class MathUtils {

    public static double vectorLength(float[] v) {
        return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
    }

    /**
     * Like the Arduino map function that re-maps a number from one range to another.
     * https://www.arduino.cc/reference/en/language/functions/math/map/
     * @param x
     * @param in_min
     * @param in_max
     * @param out_min
     * @param out_max
     * @return
     */
    public static long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    /**
     * Like the Arduino map function that re-maps a number from one range to another.
     * https://www.arduino.cc/reference/en/language/functions/math/map/
     * @param x
     * @param in_min
     * @param in_max
     * @param out_min
     * @param out_max
     * @return
     */
    public static float map(float x, float in_min, float in_max, float out_min, float out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}