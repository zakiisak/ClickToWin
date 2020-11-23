package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundSystem {

    private Sound bop, tap, explosion;
    private Map<Sound, Integer> timesPlayed = new HashMap<Sound, Integer>();
    private int timesPlayedDamageWithinQuarterSecond = 0;

    public void init()
    {
        bop = Gdx.audio.newSound(Gdx.files.internal("res/bop.ogg"));
        tap = Gdx.audio.newSound(Gdx.files.internal("res/Explosion.ogg"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("res/Tap.ogg"));
    }

    public void playDamage(final float hpPercentage)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(timesPlayedDamageWithinQuarterSecond < 10) {
                    bop.play(1.0f, 1.0f + (1.0f - hpPercentage), 0);
                    tap.play(1.0f);
                    timesPlayedDamageWithinQuarterSecond++;

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timesPlayedDamageWithinQuarterSecond--;
                }
            }
        }).start();
    }

    public void playExplosion()
    {
        explosion.play(0.5f);
    }

    public void dispose()
    {
        bop.dispose();
        tap.dispose();
        explosion.dispose();
    }

}
