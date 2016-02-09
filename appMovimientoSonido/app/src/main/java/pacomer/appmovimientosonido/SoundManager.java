package pacomer.appmovimientosonido;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @class SoundManager
 * Created by Mer on 07/02/2016.
 * Gestor para los sonidos a reproducir.
 */
public class SoundManager {
    private Context pContext;
    private SoundPool sndPool;
    private float rate=1.0f;
    private float leftVolume=1.0f;
    private float rightVolime=1.0f;

    public SoundManager(Context appContext){
        sndPool=new SoundPool(16, AudioManager.STREAM_MUSIC,100);   //
        pContext=appContext;
    }

    /**
     * @method load
     * @param idSonido sonido en res que se quiera cargar
     * @return devuelve el id de dicho sonido
     *          Carga la canción con el id correspondiente.
     */

    public int load(int idSonido) {
        return sndPool.load(pContext,idSonido,1);
    }

    /**
     * @method play
     * @param idSonido id del sonido
     *                 Reproduce el sonido correspondiente el id con los valores de la clase.
     */
    public void play(int idSonido){
        sndPool.play(idSonido,leftVolume,rightVolime,1,0,rate);
    }

    /**
     * @method stop
     * @param idSonido id del sonido
     *                 Para la reproducción del sonido correspondiente.
     */
    public void stop(int idSonido) {sndPool.stop(idSonido);}
}
