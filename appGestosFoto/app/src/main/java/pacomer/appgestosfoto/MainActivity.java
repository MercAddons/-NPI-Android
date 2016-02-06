package pacomer.appgestosfoto;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
    StringBuilder stringBuilder = new StringBuilder();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = (TextView) findViewById(R.id.strXY);
        this.textView.setText("X: ,Y: ");//texto inicial
        //evento Touch

        this.textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                stringBuilder.setLength(0);
                //si la acción que se recibe es de movimiento
                if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 200 && arg1.getY() < 300) {
                        textView.setText("Pulsado boton1");
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 200 && arg1.getY() < 300) {
                        textView.setText("Pulsado boton2");
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 200 && arg1.getY() < 300) {
                        textView.setText("Pulsado boton3");
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 440 && arg1.getY() < 540) {
                        textView.setText("Pulsado boton4");
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 440 && arg1.getY() < 540) {
                        textView.setText("Pulsado boton5");
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 440 && arg1.getY() < 540) {
                        textView.setText("Pulsado boton6");
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 680 && arg1.getY() < 780) {
                        textView.setText("Pulsado boton7");
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 680 && arg1.getY() < 780) {
                        textView.setText("Pulsado boton8");
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 680 && arg1.getY() < 780) {
                        textView.setText("Pulsado boton9");
                    }


                    stringBuilder.append("Moviendo, X:" + arg1.getX() + ", Y:" + arg1.getY());
                } else {
                    //LIMPIAR VECTOR
                }
                //Se muestra en pantalla
                // textView.setText(stringBuilder.toString());
                return true;
            }
        });
    }

}

