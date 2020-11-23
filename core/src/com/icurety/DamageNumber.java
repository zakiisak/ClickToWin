package com.icurety;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigInteger;

public class DamageNumber extends Entity {
    private static final float DAMP = 0.8f;
    private static final float SPEED = 10.0f;
    private static final Color COLOR = new Color(1.0f, 0.65f, 0.1f, 1);


    private String text;
    private float dx, dy;
    private BitmapFont font;

    public DamageNumber(BigInteger damage, BitmapFont font, float x, float y)
    {
        text = ClickToWin.formatBigInteger(damage);
        this.x = x;
        this.y = y;
        this.font = font;
        float degrees = (float) Math.random() * 360f;

        dx = (float) Math.cos(Math.toRadians(degrees)) * SPEED;
        dy = (float) Math.sin(Math.toRadians(degrees)) * SPEED;
    }

    @Override
    public void tickAndRender(ClickToWin ctw, SpriteBatch batch) {
        super.tickAndRender(ctw, batch);

        if(Math.abs(dx) < 0.001f && Math.abs(dy) < 0.001f)
        {
            dead = true;
        }

        x += dx;
        y += dy;

        dx *= DAMP;
        dy *= DAMP;

        Color c = batch.getColor().cpy();
        font.setColor(COLOR.cpy());
        font.draw(batch, text, x, y);
        batch.setColor(c);
    }
}
