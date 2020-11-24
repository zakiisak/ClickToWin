package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundSystem {

    private Sound bop, tap, fuzz, upgrade, item;
    private Music explosion;
    private Music[] upgradeComments;
    private Map<Sound, Integer> timesPlayed = new HashMap<Sound, Integer>();
    private int timesPlayedDamageWithinQuarterSecond = 0;

    public void init()
    {
        bop = Gdx.audio.newSound(Gdx.files.internal("res/bop.ogg"));
        tap = Gdx.audio.newSound(Gdx.files.internal("res/Tap.ogg"));
        fuzz = Gdx.audio.newSound(Gdx.files.internal("res/fuzz.ogg"));
        upgrade = Gdx.audio.newSound(Gdx.files.internal("res/upgrade.ogg"));
        item = Gdx.audio.newSound(Gdx.files.internal("res/item.wav"));

        explosion = Gdx.audio.newMusic(Gdx.files.internal("res/exp.wav"));

        upgradeComments = new Music[4];
        upgradeComments[0] = Gdx.audio.newMusic(Gdx.files.internal("res/radical.wav"));
        upgradeComments[1] = Gdx.audio.newMusic(Gdx.files.internal("res/nice.wav"));
        upgradeComments[2] = Gdx.audio.newMusic(Gdx.files.internal("res/oh yeah.wav"));
        upgradeComments[3] = Gdx.audio.newMusic(Gdx.files.internal("res/cool story.wav"));
    }

    public void playItem()
    {
        item.play(1f);
    }

    public void stopFuzz()
    {
        fuzz.stop();
    }

    public void playFuzzAndUpgrade()
    {
        fuzz.play(0.2f);
        upgrade.play(0.5f);
        int commentIndex = (int)(Math.random() * 4d);

        upgradeComments[commentIndex].setVolume(1.0f);
        upgradeComments[commentIndex].play();

    }

    public void playDamage(final float hpPercentage)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(timesPlayedDamageWithinQuarterSecond < 5) {
                    bop.play(1.0f, 1.0f + (1.0f - hpPercentage), 0);
                    //tap.play(1.0f);
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
        explosion.stop();
        tap.stop();
        explosion.setVolume(0.2f);
        explosion.play();
    }

    public void dispose()
    {
        bop.dispose();
        tap.dispose();
        explosion.dispose();
    }

}
