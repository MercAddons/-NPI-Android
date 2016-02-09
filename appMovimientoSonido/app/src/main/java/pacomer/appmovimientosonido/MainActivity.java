package pacomer.appmovimientosonido;

/**
 * appGestosFoto
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
 * Se realizan una serie de movimientos con el movil a modo de patrón. Si se realiza correctamente
 * suena un determinado sonido (Inicio comecocos) si falla suena la muerte de Pacman
 */
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @class MainActivity
 * Clase pricnipal de la aplicación. Implementa de SensorEventListener para hacer uso del
 * acelerómetro
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;    /*Para gestionar el sensor acelerómetro*/
    private TextView pos;           /*Comunicación con el usuario*/
    private int muerte,inicio,fallaste;     /*Muerte e inicio llevan el id correspondiente a los sonidos
                    para cuando se falle o se acierte el gesto, fallaste es un contador*/
    SoundManager sound;     /*Objeto de la clase SoundManager para gestionar los sonidos a reproducir*/
    Boolean reproducir=false,posicion, pos1, pos2, pos3;    /*Variables de control*/
    float xvalue, yvalue, zvalue;           /*Variables de control*/
    private float vx,vy,vz,precision=1f, movPrec=3.f;   /*precision*/


    @Override
    /**
     * Inicializador
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound=new SoundManager(getApplicationContext());    //Inicializa el gestor de sonidos
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);     //Establece canal de sonido a reproducir
        muerte=sound.load(R.raw.pacman);    //Asocia id con sonido
        inicio=sound.load(R.raw.inicial);
        pos=(TextView) findViewById(R.id.textView4);
        pos.setText(R.string.realiza);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE); //Se inicializa el sensor
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);     //Crea un Listener para el acelerómetro
        pos1=pos2=pos3=false;   //Ninguna posición se ha realizado del gesto.
        posicion=false;         //El gesto no se ha echo.
        xvalue=yvalue=zvalue=0;
        fallaste=0;

    }
    @Override
    /**
     * @method onSensorChanged
     * @param event valor que devuelve cuando se produce un cambio en el sensor
     *              Controla los gestos que se hace con el dispositivo gracias a los valores
     *              que devuelve el acelerómetro
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if (!posicion) {        //Si el gesto falla se reinicializan valores
                xvalue = yvalue = zvalue = 0;
                pos1 = pos2 = pos3 = false;
            }
            //Posicion inicial
            if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision) &&
                    event.values[1] < (0.0f + precision) && event.values[1] > (0.0f - precision) &&
                    event.values[2] < (10.0f + precision) && event.values[2] > (10.0f - precision)
                    && !pos2) { //Si estamos en la posiciçón inicial del gesto y no se ha hecho la pos2.
                pos2 = pos3 = false;
                pos1 = true;        //Pos1 es la primera posicion. Se pone a true.
                xvalue = event.values[0];      //Se ponen valores a la x,y y z
                yvalue = event.values[1];
                zvalue = event.values[2];
                posicion = true;    //Posicion a true, para saber que no estamos fallando
                if(fallaste>0){
                    pos.setText(R.string.realiza);      //Comunicación con usuario
                }
                fallaste=0;

            } else {
                if (pos1 && event.values[1] < (0.0f + movPrec) && event.values[1] > (0.0f - movPrec) &&
                        event.values[0] > (0.0 - precision) //Si la y no cambia, y la x es positiva
                           /* && event.values[0] < (xvalue+movPrec)
                            && event.values[2] < (zvalue+movPrec) */ && !pos2) {
                    if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision)
                            && event.values[2] < (-10.0f + precision) && event.values[2] > (-10.0f - precision)) {  //Y se llega a que x esté en 0 y la z en -10
                        pos2 = true;    //Se llega a la posicion 2 del gesto y pos2 se pone a true.
                    }
                } else {
                    if (pos2 && !pos3 && event.values[0] < (0.0 + movPrec) && event.values[0] > (0.0 - movPrec) &&
                            event.values[1] > (0.0 - precision)     //Si se ha realizado la posicion 2, la x no cambia y la y se mueve en valores positivos
                            ) { //Hasta llegar a x, y =0 y z=10
                        if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision) &&
                                event.values[1] < (0.0f + precision) && event.values[1] > (0.0f - precision) &&
                                event.values[2] < (10.0f + precision) && event.values[2] > (10.0f - precision)) {
                            pos3 = true;    //Se ha realizado el gesto, y se pone pos3 y reproducir a true.
                            reproducir = true;
                        }
                    } else {
                        //Si algo falla, se pone la variable posicion a falso y todos los controladores de las posiciones
                        posicion = false;
                        pos1 = pos2 = pos3 = false;
                        fallaste++;     //Si fallaste==1, suena. Se utiliza un contador para que la cación de fallo no suene ciclicamente.
                    }
                }
            }

            if (reproducir) {   //Si se ha echo el gesto reproducir está a true.-
                pos.setText(R.string.acierto);
                sound.play(inicio);     //Suena cancion
                reproducir = false;
                pos1 = pos2 = pos3 = false;
                posicion = false;
                fallaste=2;
            }
            if(fallaste==1) {
                pos.setText(R.string.fallaste); //suena cancion
                sound.stop(inicio);
                sound.play(muerte);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
