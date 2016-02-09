package pacomer.appmovimientosonido;

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

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private TextView pos;
    private int muerte,inicio,fallaste;
    SoundManager sound;
    Boolean reproducir=false,posicion, pos1, pos2, pos3;
    float xvalue, yvalue, zvalue;
    private float vx,vy,vz,precision=1f, movPrec=3.f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound=new SoundManager(getApplicationContext());
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        muerte=sound.load(R.raw.pacman);
        inicio=sound.load(R.raw.inicial);
        pos=(TextView) findViewById(R.id.textView4);
        pos.setText("Realiza el gesto");
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        pos1=pos2=pos3=false;
        posicion=false;
        xvalue=yvalue=zvalue=0;
        fallaste=0;

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if (!posicion) {
                xvalue = yvalue = zvalue = 0;
                pos1 = pos2 = pos3 = false;
            }
            //Posicion inicial
            if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision) &&
                    event.values[1] < (0.0f + precision) && event.values[1] > (0.0f - precision) &&
                    event.values[2] < (10.0f + precision) && event.values[2] > (10.0f - precision)
                    && !pos2) {
                pos2 = pos3 = false;
                pos1 = true;
                xvalue = event.values[0];
                yvalue = event.values[1];
                zvalue = event.values[2];
                posicion = true;
                if(fallaste>0){
                    pos.setText("Realiza el gesto");
                }
                fallaste=0;

            } else {
                if (pos1 && event.values[1] < (0.0f + movPrec) && event.values[1] > (0.0f - movPrec) &&
                        event.values[0] > (0.0 - precision)
                           /* && event.values[0] < (xvalue+movPrec)
                            && event.values[2] < (zvalue+movPrec) */ && !pos2) {
                    if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision)
                            && event.values[2] < (-10.0f + precision) && event.values[2] > (-10.0f - precision)) {
                        pos2 = true;
                    }
                } else {
                    if (pos2 && !pos3 && event.values[0] < (0.0 + movPrec) && event.values[0] > (0.0 - movPrec) &&
                            event.values[1] > (0.0 - precision)
                            ) {
                        if (event.values[0] < (0.0f + precision) && event.values[0] > (0.0f - precision) &&
                                event.values[1] < (0.0f + precision) && event.values[1] > (0.0f - precision) &&
                                event.values[2] < (10.0f + precision) && event.values[2] > (10.0f - precision)) {
                            pos3 = true;
                            reproducir = true;
                        }
                    } else {

                        posicion = false;
                        pos1 = pos2 = pos3 = false;
                        fallaste++;
                    }
                }
            }

            if (reproducir) {
                pos.setText("GESTO CORRECTO");
                sound.play(inicio);
                reproducir = false;
                pos1 = pos2 = pos3 = false;
                posicion = false;
                fallaste=2;
            }
            if(fallaste==1) {
                pos.setText("FALLASTE");
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
