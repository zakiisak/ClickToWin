package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Icons {

    private static TextureSheet icons;

    public static void init()
    {
        icons = new TextureSheet(Gdx.files.internal("res/icons.png"), 32);
    }

    public static TextureSheet getSheet()
    {
        return icons;
    }

    public static void draw(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        icons.drawIndex(batch, index, x, y, width, height);
    }

    public static void drawSolid(SpriteBatch batch, float x, float y, float width, float height)
    {
        icons.drawIndex(batch, 14, x, y, width, height);
    }

}
