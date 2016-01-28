package pacomer.appfotovoz;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private static final int VOICE_RECOGNITION_REQUEST_CODE=1;
    private Button bt_start;
    private Vector<String> punto_cardinal;
    private Vector<Integer> error;
    private TextView punto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_start=(Button)findViewById(R.id.button1);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });
    }

    private void startVoiceRecognitionActivity(){
        Intent intent =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga el punto cardinal: ");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode== RESULT_OK){
            ArrayList<String> matches=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String [] palabras=matches.get(0).toString().split(" ");
            if(palabras[0].equals("punto")){
                punto=(TextView)findViewById(R.id.cardinal);
                punto.setText("Has dicho: "+palabras[1]);
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
}
