package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Flash extends Entity {

    private Color overlayColor;
    private float startLife;
    private int life;

    public Flash(Color overlayColor, int aliveTime){
        this.overlayColor = overlayColor;
        startLife = aliveTime;
        this.life = aliveTime;
    }

    @Override
    public void tickAndRender(ClickToWin ctw, SpriteBatch batch) {
        life--;
        if(life < 0)
        {
            dead = true;
        }
    }

    @Override
    public void renderPost(SpriteBatch batch) {
        float percentage = (float) life / startLife;
        Color c = batch.getColor().cpy();
        batch.setColor(overlayColor.r, overlayColor.g, overlayColor.b, percentage);
        Icons.drawSquare(batch, 0 - Gdx.graphics.getWidth() / 4, 0 - Gdx.graphics.getHeight() / 4, Gdx.graphics.getWidth() + Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + Gdx.graphics.getWidth() / 2);
        batch.setColor(c);
    }
}
