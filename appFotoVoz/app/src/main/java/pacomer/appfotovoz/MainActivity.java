package pacomer.appfotovoz;
/**
 * appFotoVoz
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
 * Reconoce un punto cardinal por voz e indica hacia donde está dicho punto con cierto porcentaje de error
 * también introducido por el usuario.
 */
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @class MainActivity
 * Clase principal (y única) de la aplicación. Implementa de SensorEventListener para tener
 * control sobre el sensor de orientación y poder implementar una brújula.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private static final int VOICE_RECOGNITION_REQUEST_CODE=1;
    private Button bt_start;    /*Para iniciar el reconocimiento de voz*/
    private int punto_cardinal; /*0:Norte, 1:Este, 2:Sur, 3:Oese*/
    private int error,valor_min,valor_max;
    private TextView punto;     /*Para comunicarse con el usuario*/
    private ImageView img;      /*Imagen usada para la visualización de brújula*/
    private float currentDegree=0f; /*Guarda el grado actual hacia el que apunta el móvil*/
    private SensorManager miSensorManager;  /*Para gestionar el sensor de orientación*/
    private Boolean apunta;
    @Override
    /**
     * Inicializador
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); /*Elige como interfaz activity_main.xml*/
        /*Inicializa la imágen de la brúula */
        img=(ImageView)findViewById(R.id.rosaVientos);  //Asocia con el id del ImageView deseado
        img.setImageResource(R.drawable.rosaweb);       //Se modifica la imágen
        miSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);   //Se inicializa el sensor.
        punto=(TextView)findViewById(R.id.cardinal);    //ASocia con el id del TextView deseado
        apunta=false;   //Inicialmente apunta estará a false ya que no sabemos hacia donde tiene que apuntar la brújula
        punto.setText(R.string.pulsa);  //Comunicación con el usuario
        bt_start=(Button)findViewById(R.id.button1);    //Inicializa botón asociandolo con el id del Button correspondiente
        bt_start.setOnClickListener(new View.OnClickListener() {    //Modifica el método que se llama cuando se cliquea el botón.
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });

    }
    @Override
    /**
     * Método onResume sobreescrito. Registra el Listener del sensor de orientación.
     */
    protected void onResume() {
        super.onResume();
        miSensorManager.registerListener(this, miSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    /**
     * Método onPause sobreescrito. Para el Listener del sensor de orientación.
     */
    protected void onPause() {

        super.onPause();
        miSensorManager.unregisterListener(this);
    }

    /**
     * @method startvoiceRecognitionActity
     * Inicia reconocimiento de voz. Se usa un intent para pasar información a la actividad que se
     * está inicializando.
     */
    private void startVoiceRecognitionActivity(){
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.habla);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    /**
     * @method onActivityResult
     * @param requestCode Codigo que devuelve la actividad anteriormente inicializada.
     * @param resultCode Codigo resultado.
     * @param data Información que pasa la actividad del microfono a la actividad principal.
     *             Esta función guarda las palabras reconocidas por el microfono en un array
     *             y extrae la información de estas palabras para saber a que punto cardinal
     *             tiene que apuntar y con cual porcentaje de error.
     */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode== RESULT_OK) {   //Si el micro devuelve que se ha realizado el reconocimiento con exito
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);   //matches contiene las palabras separadas por " "
            String[] palabras = matches.get(0).toString().split(" ");   //Mete cada palabra en un elemento para un manejo mejor
            apunta = false;
            if (palabras[0].equals("norte") || palabras[0].equals("Norte")) {       //Si la primera palabra es norte el punto cardinal se pone a 360 y apunta a true
                punto_cardinal = 360;
                apunta = true;
            } else if (palabras[0].equals("este") || palabras[0].equals("Este")) {   //Si la primera palabra es este, punto cardinal se pone a 90 y apunta a true
                punto_cardinal = 90;
                apunta = true;
            } else if (palabras[0].equals("sur") || palabras[0].equals("Sur")) {    //Si la primera palabra es sur, punto cardinal se pone a 180 y apunta a true
                punto_cardinal = 180;
                apunta = true;
            } else if (palabras[0].equals("oeste") || palabras[0].equals("Oeste")) {    //Si la primera palabra es oeste, punto cardinal se pone a 270 y apunta a true
                punto_cardinal = 270;
                apunta = true;
            }
            try {
                if(apunta) {        //Si se ha reconocido la primera palabra y es uno de los cuatro puntos cardinales
                    error = Integer.parseInt(palabras[1]);      //Se mete en error la segunda palabra convertida en entero.
                    punto.setText("");
                }
            } catch (Exception e) {
                punto.setText(R.string.noEntiendo); //Si no se puede, Sale un mensaje de error para que se repita el reconocimient de voz
                apunta=false;
            }

            /*A continuación se calculan la sección de la brújula que va a "Apuntar" al punto indicado*/
            if (apunta && punto_cardinal != 360) {     //Si el punto no es el norte el margne viene dado por
                Integer margen = (360 * error / 100) / 2;
                valor_max = punto_cardinal + margen;
                valor_min = punto_cardinal - margen;
            } else if (apunta && punto_cardinal == 360) {   //Si es norte viene dado por:
                Integer margen = (360 * error / 100) / 2;
                valor_max = margen;
                valor_min = punto_cardinal - margen;
            }
            if (!apunta) {      //Si no apunta, es decir, no se ha introducido correctamente el punto cardinal se muestra un mensaje de error.
                punto.setText(R.string.noEntiendo);
            }
        }
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

    @Override
    /**
     * @method onSensorChanged
     * @param event Valor que devuelve cuando se produce un vambio en sensor.
     *              Controla que el dispositivo esté apuntando o no hacia el punto cardinal
     *              establecido por el usuario.
     */
    public void onSensorChanged(SensorEvent event) {
        float grado=Math.round(event.values[0]); //Grado que devuelve el sensor de orientación
      //  punto.setText("Heading: "+Float.toString(grado));
        RotateAnimation ra=new RotateAnimation(     /*Se crea un RotateAnimation para crear la rotación
                                        para indicar que se mueve desde donde actualmente está apuntando
                                        (currentDegree) hasta el nuevo grado dado por el evento del sensor
                                        de orientación*/
                currentDegree,
                -grado,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        ra.setDuration(10);     //Tiempo que tarda en girar
        ra.setFillAfter(true);
        img.startAnimation(ra);     //Se inicia la rotación antes creada en la imagen de la brújula
        currentDegree=-grado;       //Se actualiza el grado actual del dispositivo
        if(apunta){     //Si apunta está a true
            if(punto_cardinal==360){    //Y el punto cardinal es el norte
                if((grado>=valor_min && grado <=360) || (grado>=0 && grado<=valor_max)){    //Sección de error
                    punto.setText(R.string.apunta);     //Se comunica al usuario que está apuntando hacia donde quiere
                }
                else
                    punto.setText(R.string.noApunta);       //Se conmunica al usuario que no está apuntando hacia donde quiere
            }else if(grado<=valor_max && grado>=valor_min){     //Si no lo mismo. Se comprueba si se está dentro de la sección de error y se comunica al usuario si está o no apuntando.
                punto.setText(R.string.apunta);
            }
            else punto.setText(R.string.noApunta);
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
