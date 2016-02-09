package pacomer.appgestosfoto;
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
 * Se realiza un gesto y si se hace correctamente, se lanza la cámara.
 */
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

/**
 * @class MainActivity
 * Clase principal y única de la aplicación.
 *
 */
public class MainActivity extends Activity {
    Integer v[] = {7,5,2,3,6,4,1,9,8};      //Orden de los puntos que tiene que reconocer el patrón.
    Vector<Integer> c= new Vector();    //El vector donde se va actualizando que puntos del patrón se han reconocido.
    TextView textView;      //Esto sirve para reconocer que pulsaciones se están haciendo en la pantalla

    @Override
    /**
     * Inicializador.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = (TextView) findViewById(R.id.strXY);        //Se inicializa y asocia el TextView al TextView deseado.
        //evento Touch

        this.textView.setOnTouchListener(new View.OnTouchListener() { //Sobreescribe el método por defecto cuando se toca la pantalla.
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {

                //si la acción que se recibe es de movimiento
                if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    //SE introduce en c, el punto del patrón en el que esté situado el dedo.
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 200 && arg1.getY() < 300) {
                        if(c.size()>0) {
                            if (!c.contains(1)) { c.addElement(1);}
                        }else{
                            c.addElement(1);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 200 && arg1.getY() < 300) {

                        if(c.size()>0){
                            if(!c.contains(2)){c.addElement(2);};
                        }else{
                            c.addElement(2);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 200 && arg1.getY() < 300) {
                        if(c.size()>0){
                            if(!c.contains(3)){c.addElement(3);};
                        }else{
                            c.addElement(3);
                        }
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(!c.contains(4)){c.addElement(4);};
                        }else{
                            c.addElement(4);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(!c.contains(5)){c.addElement(5);};
                        }else{
                            c.addElement(5);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 440 && arg1.getY() < 540) {
                        if(c.size()>0){
                            if(!c.contains(6)){c.addElement(6);};
                        }else{
                            c.addElement(6);
                        }
                    }
                    if (arg1.getX() > 100 && arg1.getX() < 180 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(!c.contains(7)){c.addElement(7);};
                        }else{
                            c.addElement(7);
                        }
                    }
                    if (arg1.getX() > 290 && arg1.getX() < 370 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(!c.contains(8)){c.addElement(8);};
                        }else{
                            c.addElement(8);
                        }
                    }
                    if (arg1.getX() > 505 && arg1.getX() < 585 && arg1.getY() > 680 && arg1.getY() < 780) {
                        if(c.size()>0){
                            if(!c.contains(9)){c.addElement(9);};
                        }else{
                            c.addElement(9);
                        }
                    }

                } else {
                    if(c.size() == 9){     //Si c, tiene el mismo tamaño que el patrón solucion
                        Boolean igual = true;
                        for (Integer i=0;i<9 && igual;i++){    //Se comprueba si los puntos siguen el mismo orden
                            if(v[i]!=c.elementAt(i)){
                                igual = false;
                            }
                        }
                        if(igual){
                            textView.setText(R.string.correcto);
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
                            textView.setText(R.string.incorrecto);
                        }
                    } else {//Si se levanta el dedo de la pantalla
                        if(arg1.getAction() == MotionEvent.ACTION_UP){
                            textView.setText(R.string.incorrecto);
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
