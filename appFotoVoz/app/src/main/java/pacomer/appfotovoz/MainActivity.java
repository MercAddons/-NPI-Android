package pacomer.appfotovoz;

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

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private static final int VOICE_RECOGNITION_REQUEST_CODE=1;
    private Button bt_start;
    private int punto_cardinal;
    private int error,valor_min,valor_max;
    private TextView punto,debug;
    private ImageView img;
    private float currentDegree=0f;
    private SensorManager miSensorManager;
    private Boolean apunta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.rosaVientos);
        img.setImageResource(R.drawable.rosaweb);
        miSensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        punto=(TextView)findViewById(R.id.cardinal);
        debug=(TextView)findViewById(R.id.textView);
        apunta=false;
        punto.setText("Pulse micro para hablar");
        bt_start=(Button)findViewById(R.id.button1);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        miSensorManager.registerListener(this, miSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        miSensorManager.unregisterListener(this);
    }
    private void startVoiceRecognitionActivity(){
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga el punto cardinal: ");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode== RESULT_OK){
            ArrayList<String> matches=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String [] palabras=matches.get(0).toString().split(" ");
            apunta = false;
            if(palabras[0].equals("norte") || palabras[0].equals("Norte")){
                punto_cardinal=360;
                punto.setText("JIJI");
                apunta=true;
            }
            else if(palabras[0].equals("este") || palabras[0].equals("Este")){
                punto_cardinal=90;
                apunta=true;
            }
            else if(palabras[0].equals("sur") || palabras[0].equals("Sur")){
                punto_cardinal=180;
                apunta=true;
            }
            else if(palabras[0].equals("oeste") || palabras[0].equals("Oeste")){
                punto_cardinal=270;
                apunta=true;
            }
            try{
                error=Integer.parseInt(palabras[1]);
                debug.setText("");
            }catch (Exception e){
                debug.setText("No se entendio el numero, digalo otra vez.");
            }



            if(apunta && punto_cardinal!=360){
                Integer margen = (360*error/100)/2;
                valor_max=punto_cardinal+margen;
                valor_min=punto_cardinal-margen;
            }else if(apunta && punto_cardinal==360){
                Integer margen = (360*error/100)/2;
                valor_max= margen;
                valor_min=punto_cardinal-margen;
            }
            if(!apunta){
                punto.setText("NO TE ENTIENDO");
                debug.setText(palabras[0]);
            }
            else
                punto.setText("IJALSDJLAKSDJA");
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
    public void onSensorChanged(SensorEvent event) {
        float grado=Math.round(event.values[0]);
      //  punto.setText("Heading: "+Float.toString(grado));
        RotateAnimation ra=new RotateAnimation(
                currentDegree,
                -grado,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        ra.setDuration(10);
        ra.setFillAfter(true);
        img.startAnimation(ra);
        currentDegree=-grado;
        if(apunta){
            if(punto_cardinal==360){
                if((grado>=valor_min && grado <=360) || (grado>=0 && grado<=valor_max)){
                    punto.setText("Esta apuntando");
                }
                else
                    punto.setText("No esta apuntando");
            }else if(grado<=valor_max && grado>=valor_min){
                punto.setText("Esta apuntando");
            }
            else punto.setText("No esta apuntando");
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
