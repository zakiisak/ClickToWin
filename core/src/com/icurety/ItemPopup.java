package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ItemPopup extends Entity {
    public static final int ITEM_GOLD_MEDAL = 12 * 16 + 7;
    public static final int ITEM_SUN = 16 * 21 + 8;
    public static final int ICON_SIZE = 32 * 16;
    public static final int TRAY_ICON_SIZE = 64;

    private static final float DEST_X = 20;
    private static final float DEST_Y = 20;
    private static final float INCREMENT_X = (float) TRAY_ICON_SIZE * 1.5f;
    private static float trayAdvance = 0;

    private int moveUpTicks = 45;

    private int iconIndex;
    private float sizeOriginal, size;
    private int life = 180;
    private float destX, destY;

    private float percentageZoom = 1f;

    public ItemPopup(ClickToWin ctw, int iconIndex, boolean skipPopup)
    {
        this(ctw, iconIndex);
        if (skipPopup)
        {
            life = 0;
            moveUpTicks = 0;
            percentageZoom = 0;
        }
    }

    public ItemPopup(ClickToWin ctw, int iconIndex)
    {
        ctw.getSoundSystem().playItem();
        sizeOriginal = (int) (Math.min(Gdx.graphics.getWidth() / 4 * 3, ICON_SIZE) / 32) * (int) 32;
        this.iconIndex = iconIndex;
        this.destX = DEST_X + trayAdvance;
        this.destY = DEST_Y;
        trayAdvance += INCREMENT_X;
    }

    @Override
    public void tickAndRender(ClickToWin ctw, SpriteBatch batch) {
        size = percentageZoom * this.sizeOriginal;
        float baseX = Gdx.graphics.getWidth() / 2 - this.sizeOriginal / 2;
        float baseY = Gdx.graphics.getHeight() / 2 - this.sizeOriginal / 2;

        float diffX = destX - baseX;
        float diffY = destY - baseY;
        if(moveUpTicks > 0)
            moveUpTicks--;

        x = baseX + diffX * (1.0f - percentageZoom);
        y = baseY + diffY * (1.0f - percentageZoom) - moveUpTicks * 3;

        if(life > 0)
            life--;
        else
        {
            percentageZoom *= 0.9f;
            if(size * percentageZoom <= TRAY_ICON_SIZE)
            {
                this.size = TRAY_ICON_SIZE;
                if(deathCallback != null)
                {
                    deathCallback.run();
                    deathCallback = null;
                }
            }
            else percentageZoom *= 0.9f;
        }
    }

    @Override
    public void renderPost(SpriteBatch batch) {
        super.renderPost(batch);
        Icons.drawBackdrop(batch, iconIndex, x, y, size, size);
    }
}
