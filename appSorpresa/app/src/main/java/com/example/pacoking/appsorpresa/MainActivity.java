package com.example.pacoking.appsorpresa;
/**
 * appSorpresa
 *
 *
 *Copyright (C) 2016  Mercedes Alba Moyano y Francisco Peña Quiros
 *This program is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.

 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.

 *You should have received a copy of the GNU General Public License
 *along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * @author Mercedes Alba Moyano
 * @author Francisco Peña Quirós
 * @version 09.02.2016
 * Modifica el color de las luces según la luminosidad recibida por el sensor y, cuando algo está
 * muy próximo al dispositivo se enciende el flash.
 */
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

/**
 * @class MainActivity
 * Clase principal y única de la aplicación. Implementa SensorEventListener para hacer uso del
 * sensor de proximidad y de luminosidad.
 */

public class MainActivity extends AppCompatActivity implements SensorEventListener{


    private Camera dispCamara;      //Objeto tipo Camara para activar el flash.
    private TextView tv;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.textview);

        SensorManager s = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor sensorprox = s.getDefaultSensor(Sensor.TYPE_PROXIMITY);  //Inicializa sensor proximidad
        Sensor sensorluz = s.getDefaultSensor(Sensor.TYPE_LIGHT);       //Inicializa sensor luz
//Activar los Listener para los dos sensores
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
        if(evento.sensor == s.getDefaultSensor(Sensor.TYPE_PROXIMITY)){     //Si el sensor que recibe los cambios
            //Es el de proximidad
            float valor=Float.parseFloat(String.valueOf(evento.values[0]));
            if (valor == 0.0){      //Si algo está cerca del dispositivo se enciende el flash
                encenderFlash();
            }else{  //Si no, se apaga.
                apagarFlash();
            }
        }else if(evento.sensor == s.getDefaultSensor(Sensor.TYPE_LIGHT)){   //Si el sensor que recibe los cambios
            //es el de luz
            float valor = Float.parseFloat(String.valueOf(evento.values[0]));
            if(valor <=20.0){   //Si el valor recibido es menor que 20
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = 1.0f;   //Se incrementa la luminosidad de la pantalla
                getWindow().setAttributes(layoutParams);
                tv.setTextColor(Color.CYAN);    //El color de las letras se pone azul
            }else{
                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.screenBrightness = 0.2f;   //El brillo se pone más debil
                getWindow().setAttributes(layoutParams);
                tv.setTextColor(Color.GREEN);   //El color de las letras se pone verde.
            }
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            dispCamara = Camera.open();
        } catch (Exception e) {
            tv.setText(R.string.sinAcceso);
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

    /**
     * @method encenderFlash
     *      Enciende el flash del dispositivo con sus correspondientes comprobaciones de si tiene o
     *      si se puede encender.
     */
    public void encenderFlash(){
        tv.setText(R.string.accediendo);

        if (dispCamara != null) {
            tv.setText(R.string.camera);
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
                    tv.setText(R.string.errorLinterna);
                }
            } else {
                tv.setText(R.string.noFlash);
            }
        } else {
            tv.setText(R.string.noAccesoLinterna);
        }
    }

    /**
     * @method apagarFlash
     *         Apaga el flash con comprobaciones de acceso a la cámara
     */
    public void apagarFlash(){
        if (dispCamara != null) {
            Parameters parametrosCamara = dispCamara.getParameters();
            parametrosCamara.setFlashMode(Parameters.FLASH_MODE_OFF);
            dispCamara.setParameters(parametrosCamara);
        } else {
            tv.setText(R.string.noAccesoLinterna);
        }
    }
}
