package com.icurety;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle {

    public static final int PARTICLE_INDEX = 15;

    private Color color;
    private float x, y, velX, velY, dampFactor;
    private float size = 32;
    public boolean dead;

    public Particle(float x, float y, Color color, float velX, float velY, float dampFactor)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.velX = velX;
        this.velY = velY;
        this.dampFactor = dampFactor;
    }


    public void tickAndRender(SpriteBatch batch, TextureSheet sheet)
    {
        x += velX;
        y += velY;
        velX *= dampFactor;
        velY *= dampFactor;

        if(Math.abs(velX) < 0.01f || Math.abs(velY) < 0.01f)
        {
            dead = true;
        }

        Color c = batch.getColor().cpy();
        batch.setColor(color);
        sheet.drawIndex(batch, PARTICLE_INDEX, x - size / 2, y - size / 2, size, size);
        batch.setColor(c);

    }

}
