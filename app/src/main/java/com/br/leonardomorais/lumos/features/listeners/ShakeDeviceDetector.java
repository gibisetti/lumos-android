package com.br.leonardomorais.lumos.features.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by leonardo on 01/03/16.
 */
public class ShakeDeviceDetector implements SensorEventListener {

    private Context context;

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 400;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 1000;
    private static final int SHAKE_WAIT_NEXT_MS = 2000;

    private long lastShakeTimestamp;
    private long lastDoubleShakeTimestamp;
    private int ShakeCount;
    private OnShakeListener shakeListener;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    public ShakeDeviceDetector(Context context){
        this.context = context;
    }

    public interface OnShakeListener {
        public void onShake();
    }

    public void setOnShakeListener(OnShakeListener onShakeListener) {
        this.shakeListener = onShakeListener;
    }

    public void starListener(){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void endListener(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();

            if(lastShakeTimestamp + SHAKE_SLOP_TIME_MS > now){
                return;
            }

            if(lastShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now){
                ShakeCount = 1;
            }

            if(ShakeCount == 2 && (now-lastDoubleShakeTimestamp > SHAKE_WAIT_NEXT_MS)){
                shakeListener.onShake();
                ShakeCount = 1;
                lastDoubleShakeTimestamp = now;
            }

            lastShakeTimestamp = now;

            ShakeCount++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}