package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Enemy {
    public static final int ENEMY_SIZE = 512;

    public static List<Enemy> ENEMIES = new ArrayList<Enemy>();

    static {
        ENEMIES.add(new Enemy(16 * 16 + 5, new BigInteger("100")));
        ENEMIES.add(new Enemy(16 * 16 + 6, new BigInteger("200")));
        ENEMIES.add(new Enemy(16 * 16 + 7, new BigInteger("500")));
    }


    private float x, y;
    public boolean dead = false;

    private int icon;
    private BigInteger hp, startHp;

    public Enemy(int icon, BigInteger hp)
    {
        this.icon = icon;
        this.hp = hp;
        this.startHp = hp;
    }

    public void tickAndRender(SpriteBatch batch, TextureSheet sheet)
    {
        x = Gdx.graphics.getWidth() / 2 - ENEMY_SIZE / 2;
        y = Gdx.graphics.getHeight() / 2 - ENEMY_SIZE / 2;
        sheet.drawIndex(batch, icon, x, y, ENEMY_SIZE, ENEMY_SIZE);

        //hp bar
        final float barWidth = ENEMY_SIZE * 1.3f;
        final float barHeight = 50;
        final float barX = x + ENEMY_SIZE / 2 -barWidth / 2;
        final float barY = + ENEMY_SIZE * 0.8f;
        batch.setColor(0, 0, 0, 1);
        sheet.drawIndex(batch, 14, barX, barY, barWidth, barHeight);
        final float hpPercentage = new BigDecimal(hp).divide(new BigDecimal(startHp)).floatValue();
        batch.setColor(1.0f - hpPercentage, hpPercentage, 0.5f, 1);
        sheet.drawIndex(batch, 14, barX, barY, barWidth * hpPercentage, barHeight);
        batch.setColor(Color.WHITE);
    }

    public void damage(BigInteger amount){
        hp = hp.subtract(amount);
        if(hp.compareTo(BigInteger.ZERO) <= 0)
        {
            dead = true;
        }
    }

    public boolean checkClick(float clickX, float clickY)
    {
        if(clickX >= x && clickY >= y && clickX < x + ENEMY_SIZE && y < y + ENEMY_SIZE)
        {
            return true;
        }
        return false;
    }

}
