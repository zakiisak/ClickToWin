package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
        ENEMIES.add(new Enemy(16 * 16 + 8, new BigInteger("1000")));
        ENEMIES.add(new Enemy(16 * 16 + 9, new BigInteger("2000")));
        ENEMIES.add(new Enemy(16 * 16 + 10, new BigInteger("3000")));
        ENEMIES.add(new Enemy(16 * 16 + 11, new BigInteger("5000")));
        ENEMIES.add(new Enemy(16 * 16 + 12, new BigInteger("8000")));
    }


    private float x, y;
    public boolean dead = false;

    private int icon;
    private BigInteger hp, startHp;
    private Color overlayColor = Color.WHITE;

    public Enemy(int icon, BigInteger hp)
    {
        this.icon = icon;
        this.hp = hp;
        this.startHp = hp;
    }

    public void tickAndRender(SpriteBatch batch, TextureSheet sheet)
    {
        overlayColor = overlayColor.lerp(1, 1, 1, 1, 0.05f);

        x = Gdx.graphics.getWidth() / 2 - ENEMY_SIZE / 2;
        y = Gdx.graphics.getHeight() / 2 - ENEMY_SIZE / 2;
        batch.setColor(overlayColor);
        sheet.drawIndex(batch, icon, x, y, ENEMY_SIZE, ENEMY_SIZE);

        //hp bar
        final float barWidth = ENEMY_SIZE * 1.3f;
        final float barHeight = 50;
        final float barX = x + ENEMY_SIZE / 2 -barWidth / 2;
        final float barY = + ENEMY_SIZE * 0.8f;
        batch.setColor(0, 0, 0, 0.6f);
        sheet.drawIndex(batch, 14, barX, barY, barWidth, barHeight);
        final float hpPercentage = new BigDecimal(hp).divide(new BigDecimal(startHp)).floatValue();
        batch.setColor(1.0f - hpPercentage, hpPercentage, 0.5f, 1);
        sheet.drawIndex(batch, 14, barX, barY, barWidth * hpPercentage, barHeight);
        batch.setColor(Color.WHITE);
    }

    public void damage(BigInteger amount){
        hp = hp.subtract(amount);
        overlayColor = Color.RED.cpy();
        if(hp.compareTo(BigInteger.ZERO) <= 0)
        {
            dead = true;
        }
    }

    private boolean checkPixelCollision(TextureSheet sheet, float clickXOnScreen, float clickYOnScreen)
    {
        clickYOnScreen = Gdx.graphics.getHeight() - clickYOnScreen;
        Pixmap map = sheet.getPixMap();
        int relativeX = (int) (clickXOnScreen - x);
        int relativeY = (int) (clickYOnScreen - y); //invert the y because of the screen coord system
        int xIndex = relativeX / (ENEMY_SIZE / (int) Math.round(sheet.indexSize));
        int yIndex = relativeY / (ENEMY_SIZE / (int) Math.round(sheet.indexSize));
        System.out.println("xIndex: " + xIndex + ", yIndex: " + yIndex);
        System.out.println("relativeX: " + relativeX + ", relativeY: " + relativeY);
        Color c = new Color();
        Color.rgba8888ToColor(c, sheet.getPixel(icon, xIndex, yIndex));
        if(c.a > 0.5f)
            return true;
        return false;
    }

    public boolean checkClick(TextureSheet sheet, float clickX, float clickY)
    {
        if(clickX >= x && clickY >= y && clickX < x + ENEMY_SIZE && y < y + ENEMY_SIZE)
        {
            return checkPixelCollision(sheet, clickX, clickY);
        }
        return false;
    }

    public Enemy cpy()
    {
        return new Enemy(icon, startHp);
    }

}
