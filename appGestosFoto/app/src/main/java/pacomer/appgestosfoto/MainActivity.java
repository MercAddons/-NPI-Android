package pacomer.appgestosfoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.Vector;

public class MainActivity extends Activity {
    StringBuilder stringBuilder = new StringBuilder();
    Integer v[] = {8,9,5,1,4,5,6,3,2,5,7};
    Vector<Integer> c= new Vector();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = (TextView) findViewById(R.id.strXY);
        //evento Touch

        this.textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                stringBuilder.setLength(0);
                //si la acción que se recibe es de movimiento
                if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 200 && arg1.getY() < 300) {
                        if(c.size()>0) {
                            if (c.elementAt(c.size() - 1) != 1) { c.addElement(1);};
                        }else{
                            c.addElement(1);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 200 && arg1.getY() < 300) {

                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=2){c.addElement(2);};
                        }else{
                            c.addElement(2);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 200 && arg1.getY() < 300) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=3){c.addElement(3);};
                        }else{
                            c.addElement(3);
                        }
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=4){c.addElement(4);};
                        }else{
                            c.addElement(4);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=5){c.addElement(5);};
                        }else{
                            c.addElement(5);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=6){c.addElement(6);};
                        }else{
                            c.addElement(6);
                        }
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=7){c.addElement(7);};
                        }else{
                            c.addElement(7);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=8){c.addElement(8);};
                        }else{
                            c.addElement(8);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(c.elementAt(c.size()-1)!=9){c.addElement(9);};
                        }else{
                            c.addElement(9);
                        }
                    }

                } else {
                    if(c.size() == 11){
                        Boolean igual = true;
                        for (Integer i=0;i<10 && igual;i++){
                            if(v[i]!=c.elementAt(i)){
                                igual = false;
                            }
                        }
                        if(igual){
                            textView.setText("Se hizo el patron correcto, ahora se activara ");
                            //REALIZAR LLAMADA A CAMARA MOVIL

                            Intent cameraIntent = new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            //Creamos una carpeta en la memeria del terminal
                            File imagesFolder = new File(
                                    Environment.getExternalStorageDirectory(), "AppGestosFoto");
                            imagesFolder.mkdirs();
                            //añadimos el nombre de la imagen
                            File image = new File(imagesFolder, "foto.jpg");
                            Uri uriSavedImage = Uri.fromFile(image);
                            //Le decimos al Intent que queremos grabar la imagen
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                            //Lanzamos la aplicacion de la camara con retorno (forResult)
                            startActivityForResult(cameraIntent, 0);
                        }else{
                            textView.setText("No se hizo el patron correcto");
                        }
                    } else {
                        if(arg1.getAction() == MotionEvent.ACTION_UP){
                            textView.setText("No se hizo el patron correcto");
                        }
                    }
                    //LIMPIAR VECTOR
                    c.clear();
                }
                //Se muestra en pantalla

                return true;
            }
        });
    }

}
