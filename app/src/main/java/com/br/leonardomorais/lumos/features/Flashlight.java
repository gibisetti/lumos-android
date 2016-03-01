package com.br.leonardomorais.lumos.features;

import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

import com.br.leonardomorais.lumos.R;

import java.util.List;

/**
 * Created by leonardo on 01/03/16.
 */
public class Flashlight {
    private Camera camera;
    private Camera.Parameters parameters;
    private boolean hasFlash = false;

    public Flashlight(Context context){
        try {
            camera = Camera.open();
            if(!hasFlash()){
                Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.error_flash), Toast.LENGTH_LONG).show();
            }else{
                hasFlash = true;
            }
            parameters = camera.getParameters();
        } catch (RuntimeException e) {
            Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.error_camera) + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void turnOn(){
        if(hasFlash){
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
        }
    }

    public void turnOff(){
        if(hasFlash){
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
        }
    }

    public void release(){
        camera.release();
    }

    private boolean hasFlash() {
        parameters = camera.getParameters();

        if (parameters.getFlashMode() == null) {
            return false;
        }

        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        if (supportedFlashModes == null || supportedFlashModes.isEmpty() || supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF)) {
            return false;
        }
        return true;
    }
}
