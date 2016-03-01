package com.br.leonardomorais.lumos.features;

import android.content.Context;
import android.media.MediaPlayer;

import com.br.leonardomorais.lumos.R;

/**
 * Created by leonardo on 01/03/16.
 */
public class SpellSound {
    private Context context;
    private MediaPlayer mp;

    public SpellSound(Context context){
        this.context = context;
    }

    public void playSoundOn(){
        playSpellSound(R.raw.lumos_on);
    }

    public void playSoundOff(){
        playSpellSound(R.raw.lumos_off);
    }

    public void playSpellSound(int soundRaw){
        mp = MediaPlayer.create(context, soundRaw);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }
}
