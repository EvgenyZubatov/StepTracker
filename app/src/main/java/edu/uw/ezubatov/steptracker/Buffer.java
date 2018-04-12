package edu.uw.ezubatov.steptracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugen on 4/11/2018.
 */

public class Buffer {
    private int size;
    private int window;

    private int j = 0;

    private double C = 0.8;
    private double G = 11;

    private List<Double> data;
    private List<Double> runningAverage;
    private List<Double> peaks;

    private double total = 0;

    public Buffer(int size, int window) {
        this.size = size;
        this.window = window;
        this.data = new ArrayList<>(size+1);
        this.runningAverage = new ArrayList<>(size+1);
        this.peaks = new ArrayList<>(size+1);
    }

    public int addSample(double x) {
        j++;
        total += x;
        data.add(x);
        if (data.size() > size) {
            total -= data.get(0);
            data.remove(0);
        }

        runningAverage.add(total/data.size());
        if (runningAverage.size() > size) {
            runningAverage.remove(0);
        }

        if (j > window) {
            j = 0;
            return peakDetection();
        }

        return 0;
    }

    private int peakDetection() {
        int peakCount = 0;
        double peakTotal = 0;
        for (int i=1; i < window; i++) {
            double forwardSlope = runningAverage.get(i+1) - runningAverage.get(i);
            double backwardSlope = runningAverage.get(i) - runningAverage.get(i-1);

            if (forwardSlope < 0 && backwardSlope > 0) {
                peakCount++;
                peakTotal += runningAverage.get(i);
            }
        }

        double peakMean = peakTotal/peakCount;

        int stepCount = 0;
        for (int i=1; i < window; i++) {
            double forwardSlope = runningAverage.get(i+1) - runningAverage.get(i);
            double backwardSlope = runningAverage.get(i) - runningAverage.get(i-1);

            if (forwardSlope < 0 && backwardSlope > 0 &&
                    runningAverage.get(i) > C*peakMean && runningAverage.get(i) > G) {
                stepCount++;

                this.peaks.add(runningAverage.get(i));
                if (peaks.size() > size) {
                    this.peaks.remove(0);
                }
            }
        }

        return stepCount;
    }
}
