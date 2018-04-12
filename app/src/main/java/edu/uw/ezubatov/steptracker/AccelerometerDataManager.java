package edu.uw.ezubatov.steptracker;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Eugen on 4/9/2018.
 */

public class AccelerometerDataManager {
    private static int MAX_ACCEL_VALUE = 30;

    // Increasing the size of the smoothing window will increasingly smooth the accel signal; however,
    // at a cost of responsiveness. Play around with different window sizes: 20, 50, 100...
    // Note that I've implemented a simple Mean Filter smoothing algorithm
    private static int SMOOTHING_WINDOW_SIZE = 20;

    private float _rawAccelValues[] = new float[3];

    // smoothing accelerometer signal stuff
    private float _accelValueHistory[][] = new float[3][SMOOTHING_WINDOW_SIZE];
    private float _runningAccelTotal[] = new float[3];
    private float _curAccelAvg[] = new float[3];
    private int _curReadIndex = 0;

    private static final AccelerometerDataManager ourInstance = new AccelerometerDataManager();

    public static AccelerometerDataManager getInstance() {
        return ourInstance;
    }

    private AccelerometerDataManager() {
    }

    public void onNewData(float[] values) {
        _rawAccelValues[0] = values[0];
        _rawAccelValues[1] = values[1];
        _rawAccelValues[2] = values[2];

        // Smoothing algorithm adapted from: https://www.arduino.cc/en/Tutorial/Smoothing
        for (int i = 0; i < 3; i++) {
            _runningAccelTotal[i] = _runningAccelTotal[i] - _accelValueHistory[i][_curReadIndex];
            _accelValueHistory[i][_curReadIndex] = _rawAccelValues[i];
            _runningAccelTotal[i] = _runningAccelTotal[i] + _accelValueHistory[i][_curReadIndex];
            _curAccelAvg[i] = _runningAccelTotal[i] / SMOOTHING_WINDOW_SIZE;
        }

        _curReadIndex++;
        if(_curReadIndex >= SMOOTHING_WINDOW_SIZE){
            _curReadIndex = 0;
        }

        setSteps();
    }

    public float[][] getHistory() {
        return _accelValueHistory;
    }

    public float[] getCurrentAverage() {
        return _curAccelAvg;
    }

    private int steps = 0;

    private void setSteps() {
        steps++;
        for (OnStepsListener l: listeners) {
            l.onSteps(steps);
        }
    }

    public interface OnStepsListener {
        // TODO: Update argument type and name
        void onSteps(int steps);
    }

    HashSet<OnStepsListener> listeners = new HashSet<>();
    public void subscribeForSteps(OnStepsListener listener) {
        listeners.add(listener);
    }
}
