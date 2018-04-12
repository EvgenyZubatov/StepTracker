package edu.uw.ezubatov.steptracker;

import java.util.HashSet;

/**
 * Created by Eugen on 4/10/2018.
 */

class SensorDataManager {
    private static final SensorDataManager ourInstance = new SensorDataManager();

    private float[] _gravity;

    private int initialSteps = 0;
    public int steps = 0;
    public int stepDetected = 0;

    static SensorDataManager getInstance() {
        return ourInstance;
    }

    private SensorDataManager() {
    }

    public void setGravity(float[] data) {
        _gravity = data;
    }

    public float[] getGravity() {
        return _gravity;
    }

    public void setStepCount(float n) {
        if (initialSteps == 0) {
            initialSteps = (int)n;
        }
        steps = (int)n - initialSteps + 1;
        for (OnAndroidStepsListener l: listeners) {
            l.onAndroidSteps(steps);
        }
    }

    public int getStepCount() {
        return steps;
    }

    public void addStep() {
        stepDetected++;
    }

    public int getStepsDetected() {
        return stepDetected;
    }

    public interface OnAndroidStepsListener {
        // TODO: Update argument type and name
        void onAndroidSteps(int steps);
    }

    HashSet<SensorDataManager.OnAndroidStepsListener> listeners = new HashSet<>();
    public void subscribeForSteps(OnAndroidStepsListener listener) {
        listeners.add(listener);
    }

}
