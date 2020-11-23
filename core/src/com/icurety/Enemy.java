package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.JsonReader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
        ENEMIES.add(new Enemy(16 * 16 + 13, new BigInteger("12000")));
        ENEMIES.add(new Enemy(16 * 15, new BigInteger("20000")));
        ENEMIES.add(new Enemy(16 * 15 + 1, new BigInteger("30000")));
        ENEMIES.add(new Enemy(16 * 15 + 2, new BigInteger("50000")));
        ENEMIES.add(new Enemy(16 * 15 + 3, new BigInteger("100000")));
        ENEMIES.add(new Enemy(16 * 15 + 4, new BigInteger("150000")));
        ENEMIES.add(new Enemy(16 * 15 + 7, new BigInteger("200000")));
        ENEMIES.add(new Enemy(16 * 15 + 9, new BigInteger("400000")));
        ENEMIES.add(new Enemy(16 * 15 + 12, new BigInteger("500000")));
        ENEMIES.add(new Enemy(16 * 15 + 13, new BigInteger("800000")));
        ENEMIES.add(new Enemy(16 * 15 + 14, new BigInteger("1000000")));

        ENEMIES.add(new Enemy(16 * 14,      new BigInteger("2000000")));
        ENEMIES.add(new Enemy(16 * 14 + 1,  new BigInteger("3000000")));
        ENEMIES.add(new Enemy(16 * 14 + 2,  new BigInteger("4000000")));
        ENEMIES.add(new Enemy(16 * 14 + 3,  new BigInteger("6000000")));
        ENEMIES.add(new Enemy(16 * 14 + 4,  new BigInteger("9000000")));
        ENEMIES.add(new Enemy(16 * 14 + 5,  new BigInteger("14000000")));
        ENEMIES.add(new Enemy(16 * 14 + 6,  new BigInteger("20000000")));
        ENEMIES.add(new Enemy(16 * 14 + 7,  new BigInteger("30000000")));
        ENEMIES.add(new Enemy(16 * 14 + 8,  new BigInteger("50000000")));
        ENEMIES.add(new Enemy(16 * 14 + 9,  new BigInteger("70000000")));
        ENEMIES.add(new Enemy(16 * 14 + 10, new BigInteger("100000000")));
        ENEMIES.add(new Enemy(16 * 14 + 12, new BigInteger("200000000")));
        ENEMIES.add(new Enemy(16 * 14 + 13, new BigInteger("400000000")));
        ENEMIES.add(new Enemy(16 * 14 + 11, new BigInteger("500000000"))); //chili

        ENEMIES.add(new Enemy(16 * 12,      new BigInteger("1000000000")));
        ENEMIES.add(new Enemy(16 * 12 + 1,  new BigInteger("2000000000")));
        ENEMIES.add(new Enemy(16 * 12 + 3,  new BigInteger("4000000000")));
        ENEMIES.add(new Enemy(16 * 12 + 4,  new BigInteger("8000000000")));
        ENEMIES.add(new Enemy(16 * 12 + 5,  new BigInteger("10000000000")));

        ENEMIES.add(new Enemy(16 * 11,      new BigInteger("200000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 1,  new BigInteger("300000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 3,  new BigInteger("500000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 4,  new BigInteger("900000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 6,  new BigInteger("1400000000000"))); //Instruments
        ENEMIES.add(new Enemy(16 * 11 + 6,  new BigInteger("2000000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 7,  new BigInteger("5000000000000")));
        ENEMIES.add(new Enemy(16 * 11 + 8,  new BigInteger("6000000000000")));

        ENEMIES.add(new Enemy(16 * 10 + 7,  new BigInteger("10000000000000"))); //binocular
        ENEMIES.add(new Enemy(16 * 10 + 8,  new BigInteger("30000000000000"))); //magnifier
        ENEMIES.add(new Enemy(16 * 10 + 9,  new BigInteger("60000000000000"))); //lantern
        ENEMIES.add(new Enemy(16 * 10 + 15, new BigInteger("10000000000000"))); //hourglass

        ENEMIES.add(new Enemy(16 * 9 + 1,   new BigInteger("13000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 2,   new BigInteger("14000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 3,   new BigInteger("15000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 4,   new BigInteger("16000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 5,   new BigInteger("17000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 6,   new BigInteger("18000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 7,   new BigInteger("20000000000000"))); //potions
        ENEMIES.add(new Enemy(16 * 9 + 8,   new BigInteger("21000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 9,   new BigInteger("22000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 10,  new BigInteger("23000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 11,  new BigInteger("24000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 12,  new BigInteger("25000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 13,  new BigInteger("26000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 14,  new BigInteger("27000000000000")));
        ENEMIES.add(new Enemy(16 * 9 + 15,  new BigInteger("50000000000000"))); //toilet roll

        ENEMIES.add(new Enemy(16 * 4 + 2,   new BigInteger("100000000000000"))); //fireplace
        ENEMIES.add(new Enemy(16 * 4 + 3,   new BigInteger("200000000000000"))); //tent
        ENEMIES.add(new Enemy(16 * 4 + 8,   new BigInteger("400000000000000"))); //robber

        ENEMIES.add(new Enemy(16 * 12 + 15, new BigInteger("1000000000000000"))); //ruby

        ENEMIES.add(new Enemy(16 * 5 + 15,  new BigInteger("2000000000000000"))); //fist
        ENEMIES.add(new Enemy(16 * 5 + 14,  new BigInteger("5000000000000000"))); //crazy red amulet

        ENEMIES.add(new Enemy(1,            new BigInteger("8000000000000000"))); //green fire
        ENEMIES.add(new Enemy(4,            new BigInteger("15000000000000000"))); //scary face
        ENEMIES.add(new Enemy(16,           new BigInteger("17000000000000000"))); //heart
        ENEMIES.add(new Enemy(16 + 3,       new BigInteger("20000000000000000"))); //brain
        ENEMIES.add(new Enemy(16 * 7 + 15,  new BigInteger("22000000000000000"))); //belt
        ENEMIES.add(new Enemy(16 * 7 + 7,   new BigInteger("25000000000000000"))); //armor
        ENEMIES.add(new Enemy(16 * 7,       new BigInteger("28000000000000000"))); //robin hood hat
        ENEMIES.add(new Enemy(16 * 7 + 2,   new BigInteger("32000000000000000"))); //helmet

        ENEMIES.add(new Enemy(16 * 3 + 7,   new BigInteger("50000000000000000"))); //living armor
        ENEMIES.add(new Enemy(16 * 3 + 8,   new BigInteger("75000000000000000"))); //blue living armor
        ENEMIES.add(new Enemy(16 * 3 + 10,  new BigInteger("125000000000000000"))); //black skull
        ENEMIES.add(new Enemy(16 * 3 + 11,  new BigInteger("150000000000000000"))); //red fist
        ENEMIES.add(new Enemy(16 * 3 + 14,  new BigInteger("200000000000000000"))); //invis man

        ENEMIES.add(new Enemy(0,            new BigInteger("250000000000000000"))); //skull with bones
        ENEMIES.add(new Enemy(9,            new BigInteger("300000000000000000"))); //red skull

        ENEMIES.add(new Enemy(16 * 21 + 11, new BigInteger("500000000000000000"))); //freeze
        ENEMIES.add(new Enemy(16 * 21 + 10, new BigInteger("750000000000000000"))); //moon
        ENEMIES.add(new Enemy(16 * 21 + 7,  new BigInteger("1000000000000000000"))); //sunrise
        ENEMIES.add(new Enemy(16 * 21 + 8,  new BigInteger("2000000000000000000"))); //sun
        ENEMIES.add(new Enemy(16 * 3 + 15,  new BigInteger("10000000000000000000"))); //sun blast

        ENEMIES.add(new Enemy(16 * 19 + 10, new BigInteger("20000000000000000000"))); //horse
        ENEMIES.add(new Enemy(16 * 16 + 3,  new BigInteger("30000000000000000000"))); //pre last fish

        //last enemy
        ENEMIES.add(new Enemy(16 * 16 + 4,  new BigInteger("10000000000000000000000")));
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

    public void tickAndRender(SpriteBatch batch)
    {
        overlayColor = overlayColor.lerp(1, 1, 1, 1, 0.05f);

        x = Gdx.graphics.getWidth() / 2 - ENEMY_SIZE / 2;
        y = Gdx.graphics.getHeight() / 2 - ENEMY_SIZE / 2;
        batch.setColor(overlayColor);
        Icons.draw(batch, icon, x, y, ENEMY_SIZE, ENEMY_SIZE);

        //hp bar
        final float barWidth = ENEMY_SIZE * 1.3f;
        final float barHeight = 50;
        final float barX = x + ENEMY_SIZE / 2 -barWidth / 2;
        final float barY = + ENEMY_SIZE * 0.8f;
        batch.setColor(1, 1, 1, 1);
        Icons.drawSolid(batch, barX - 1, barY - 1, barWidth + 2, barHeight + 2); //white outline
        batch.setColor(0, 0, 0, 0.6f);
        Icons.drawSolid(batch, barX, barY, barWidth, barHeight);
        final float hpPercentage = getHpPercentage();
        batch.setColor(1.0f - hpPercentage, hpPercentage, 0.5f, 1);
        Icons.drawSolid(batch, barX, barY, barWidth * hpPercentage, barHeight);
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

    private boolean checkPixelCollision(float clickXOnScreen, float clickYOnScreen)
    {
        TextureSheet sheet = Icons.getSheet();
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

    public boolean checkClick(float clickX, float clickY)
    {
        if(clickX >= x && clickY >= y && clickX < x + ENEMY_SIZE && y < y + ENEMY_SIZE)
        {
            return checkPixelCollision(clickX, clickY);
        }
        return false;
    }

    public Enemy cpy()
    {
        return new Enemy(icon, startHp);
    }

    public Enemy cpyWithCurrentHp()
    {
        Enemy enemy = new Enemy(icon, startHp);
        enemy.hp = hp;
        return enemy;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getHpPercentage()
    {
        return new BigDecimal(hp).divide(new BigDecimal(startHp), 2, RoundingMode.HALF_UP).floatValue();
    }

}
