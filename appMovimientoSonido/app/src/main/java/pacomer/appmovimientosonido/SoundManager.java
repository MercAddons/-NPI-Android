package pacomer.appmovimientosonido;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Mer on 07/02/2016.
 */
public class SoundManager {
    private Context pContext;
    private SoundPool sndPool;
    private float rate=1.0f;
    private float leftVolume=1.0f;
    private float rightVolime=1.0f;

    public SoundManager(Context appContext){
        sndPool=new SoundPool(16, AudioManager.STREAM_MUSIC,100);
        pContext=appContext;
    }
    public int load(int idSonido) {
        return sndPool.load(pContext,idSonido,1);
    }

    public void play(int idSonido){
        sndPool.play(idSonido,leftVolume,rightVolime,1,0,rate);
    }
    public void stop(int idSonido) {sndPool.stop(idSonido);}
}
