package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Explosion extends Entity {
    public static final float SIZE = 2048;

    private TextureSheet explosionSheet;
    private int index = 0;
    private int animationTick, animationDelay;
    private float size;

    public Explosion(TextureSheet sheet, int startIndex, float x, float y, int animationDelay)
    {
        this.explosionSheet = sheet;
        this.animationDelay = animationDelay;
        if(SIZE > Gdx.graphics.getWidth())
            size = SIZE / 2;
        this.x = x;
        this.y = y;
        this.index = startIndex;
    }

    @Override
    public void tickAndRender(ClickToWin ctw, SpriteBatch batch) {
        super.tickAndRender(ctw, batch);

        if(animationTick > animationDelay)
        {
            index++;
            animationTick = 0;
        }
        animationTick++;

        if(index >= explosionSheet.getTextureCount())
        {
            dead = true;
            index = explosionSheet.getTextureCount() - 1;
        }
        else
        {
            explosionSheet.drawIndex(batch, index, x - size / 2, y - size / 2, size, size);
        }
    }
}
