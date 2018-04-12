package edu.uw.ezubatov.steptracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Eugen on 4/9/2018.
 */

public class AccelerometerView extends View implements SensorEventListener {
    // accelerometer stuff
    private SensorManager _sensorManager;
    private Sensor _accelSensor;

    private Sensor _gravitySensor;
    private Sensor _stepCountSensor;
    private Sensor _stepDetectionSensor;

    public AccelerometerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AccelerometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AccelerometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public AccelerometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // See https://developer.android.com/guide/topics/sensors/sensors_motion.html
        _sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        _accelSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        _gravitySensor = _sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        _stepCountSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        _stepDetectionSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // The official Google accelerometer example code found here:
        //   https://github.com/android/platform_development/blob/master/samples/AccelerometerPlay/src/com/example/android/accelerometerplay/AccelerometerPlayActivity.java
        // explains that it is not necessary to get accelerometer events at a very high rate, by using a slower rate (SENSOR_DELAY_UI), we get an
        // automatic low-pass filter, which "extracts" the gravity component of the acceleration. As an added benefit, we use less power and
        // CPU resources. I haven't experimented with this, so can't be sure.
        // See also: https://developer.android.com/reference/android/hardware/SensorManager.html#SENSOR_DELAY_UI
        _sensorManager.registerListener(this, _accelSensor, SensorManager.SENSOR_DELAY_GAME);

        _sensorManager.registerListener(this, _gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        _sensorManager.registerListener(this, _stepCountSensor, SensorManager.SENSOR_DELAY_GAME);

        if (_stepDetectionSensor != null) {
            _sensorManager.registerListener(this, _stepDetectionSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                AccelerometerDataManager.getInstance().onNewData(sensorEvent.values);
                StepsDetector.getInstance().onNewData(sensorEvent.values);
                break;
            case Sensor.TYPE_GRAVITY:
                SensorDataManager.getInstance().setGravity(sensorEvent.values);
                break;
            case Sensor.TYPE_STEP_COUNTER:
                SensorDataManager.getInstance().setStepCount(sensorEvent.values[0]);
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                SensorDataManager.getInstance().addStep();
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
