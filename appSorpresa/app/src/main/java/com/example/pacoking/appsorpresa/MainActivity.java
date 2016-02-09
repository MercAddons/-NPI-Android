package com.example.pacoking.appsorpresa;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private Button btEncenderLinterna;
    private Button btApagarLinterna;
    private Camera dispCamara;
    private TextView tv;
    private Integer colordefeat = -14414579;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textview);

        SensorManager s = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor sensorprox = s.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Sensor sensorluz = s.getDefaultSensor(Sensor.TYPE_LIGHT);

        s.registerListener(MainActivity.this,sensorprox,SensorManager.SENSOR_DELAY_NORMAL);
        s.registerListener(MainActivity.this,sensorluz,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent evento) {
        // TODO Auto-generated method stub
        SensorManager s = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(evento.sensor == s.getDefaultSensor(Sensor.TYPE_PROXIMITY)){
            float valor=Float.parseFloat(String.valueOf(evento.values[0]));
            if (valor == 0.0){
                encenderFlash();
            }else{
                apagarFlash();
            }
        }else if(evento.sensor == s.getDefaultSensor(Sensor.TYPE_LIGHT)){
            float valor = Float.parseFloat(String.valueOf(evento.values[0]));
            if(valor <=20.0){
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = 1.0f;
                getWindow().setAttributes(layoutParams);
                tv.setTextColor(Color.CYAN);
            }else{
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = 0.2f;
                getWindow().setAttributes(layoutParams);
                tv.setTextColor(Color.GREEN);
            }
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dispCamara = Camera.open();
        } catch (Exception e) {
            tv.setText("No se ha podido acceder a la cámara");
        }
    }

    @Override
    protected void onPause() {
        if (dispCamara != null) {
            dispCamara.release();
            dispCamara = null;
        }
        super.onPause();
    }

    public void encenderFlash(){
        tv.setText("Accediendo a la cámara");

        if (dispCamara != null) {
            tv.setText("Cámara encontrada");
            Parameters parametrosCamara = dispCamara.getParameters();

            //Get supported flash modes
            List modosFlash = parametrosCamara.getSupportedFlashModes();


            if (modosFlash != null &&
                    modosFlash.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                //Set the flash parameter to use the torch
                parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                try {
                    dispCamara.setParameters(parametrosCamara);
                    dispCamara.startPreview();
                } catch (Exception e) {
                    tv.setText("Error al activar la linterna");
                }
            } else {
                tv.setText("El dispositivo no tiene el modo de Flash Linterna");
            }
        } else {
            tv.setText("No se ha podido acceder al Flash de la cámara");
        }
    }

    public void apagarFlash(){
        if (dispCamara != null) {
            Parameters parametrosCamara = dispCamara.getParameters();
            parametrosCamara.setFlashMode(Parameters.FLASH_MODE_OFF);
            dispCamara.setParameters(parametrosCamara);
        } else {
            tv.setText("No se ha podido acceder al Flash de la cámara");
        }
    }
}
