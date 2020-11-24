package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Particle extends Entity {

    public static final int PARTICLE_INDEX = 15;

    private Color color;
    private float velX, velY, dampFactor;
    private float size = 32;

    public Particle(float x, float y, Color color, float velX, float velY, float dampFactor)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.velX = velX;
        this.velY = velY;
        this.dampFactor = dampFactor;
    }


    public void tickAndRender(ClickToWin ctw, SpriteBatch batch)
    {
        x += velX;
        y += velY;
        velX *= dampFactor;
        velY *= dampFactor;

        float hypotenuse = (float) Math.sqrt(velX * velX + velY * velY);
        if(hypotenuse < 0.01f)
        {
            dead = true;
        }

        if(x < -200 || y < -200 || x > Gdx.graphics.getWidth() + 200 || y > Gdx.graphics.getHeight() + 200)
        {
            dead = true;
        }

        Color c = batch.getColor().cpy();
        batch.setColor(color);
        Icons.draw(batch, PARTICLE_INDEX, x - size / 2, y - size / 2, size, size);
        batch.setColor(c);


    }

}
