package edu.uw.ezubatov.steptracker;

import java.util.HashSet;

/**
 * Created by Eugen on 4/11/2018.
 */

class StepsDetector {
    Buffer buffer;

    private static final StepsDetector ourInstance = new StepsDetector();

    static StepsDetector getInstance() {
        return ourInstance;
    }

    private StepsDetector() {
        buffer = new Buffer(20,10);
    }

    public void onNewData(float[] values) {
        double l = MathUtils.vectorLength(values);
        int s = buffer.addSample(l);
        if (s != 0) {
            updateSteps(s);
        }
    }

    private int steps = 0;

    private void updateSteps(int s) {
        steps += s;
        for (OnStepsListener l: listeners) {
            l.onSteps(steps);
        }
    }

    public interface OnStepsListener {
        void onSteps(int steps);
    }

    HashSet<StepsDetector.OnStepsListener> listeners = new HashSet<>();
    public void subscribeForSteps(StepsDetector.OnStepsListener listener) {
        listeners.add(listener);
    }
}
