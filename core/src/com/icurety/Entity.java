package com.icurety;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity {

    public float x, y;
    public boolean dead;
    protected Runnable deathCallback;

    public void onDeath(ClickToWin ctw)
    {
        if(deathCallback != null)
            deathCallback.run();
    }

    public void setDeathCallback(Runnable runnable)
    {
        this.deathCallback = runnable;
    }


    public void tickAndRender(ClickToWin ctw, SpriteBatch batch)  {}
    public void renderPost(SpriteBatch batch) {}

    public boolean onSpawn(ClickToWin ctw)
    {
        return true;
    }

}
