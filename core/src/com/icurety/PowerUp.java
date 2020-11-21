package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigInteger;

public class PowerUp extends Entity {
    private static final int ICON_INDEX = 20;
    private static final float GRAVITY = 1;
    private static final int SIZE = 384;

    private float dx = 10, dy = 30;

    public PowerUp(ClickToWin ctw)
    {
        x = 300;
        y = (float) Gdx.graphics.getHeight() - (float) Gdx.graphics.getHeight() * 0.25f;
        ctw.damage = ctw.damage.add(BigInteger.valueOf(5));
    }


    @Override
    public void tickAndRender(ClickToWin ctw, SpriteBatch batch) {
        dy -= GRAVITY;

        //spawn particles
        int count = 2 + (int) (Math.random() * 5D);
        final float dampFactor = 0.90f;
        final float maxSpeed = 20.0f;
        for(int i = 0; i < count; i++)
        {
            final float degrees = (float) Math.random() * 360;
            final float dx = (float) (Math.cos(Math.toRadians(degrees)) * Math.random() * maxSpeed);
            final float dy = (float) (Math.sin(Math.toRadians(degrees)) * Math.random() * maxSpeed);
            ctw.entities.add(new Particle(x, y, new Color(1.0f, 0.75f + (float) Math.random() * 0.125f, 0, 1), dx, dy, dampFactor));
        }


        if(y < SIZE)
        {
            dead = true;
        }
        x += dx;
        y += dy;
    }

    @Override
    public void renderPost(SpriteBatch batch) {
        Icons.draw(batch, ICON_INDEX, x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }
}