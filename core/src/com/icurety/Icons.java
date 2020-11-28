package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class Icons {

    private static TextureSheet icons;
    private static TextureSheet icons_inverted;
    private static TextureSheet icons_overlay;
    private static TextureSheet icons_backdrop;

    public static void init()
    {
        icons = new TextureSheet(Gdx.files.internal("res/icons.png"), 32);
        icons_inverted = new TextureSheet(Gdx.files.internal("res/icons_inverted.png"), 32);
        icons_overlay = TextureSheet.createOpaqueSheet(Gdx.files.internal("res/icons.png"), Color.WHITE, 32, 0.5f);
        icons_backdrop = new TextureSheet(Gdx.files.internal("res/icons_backdrop.png"), 32);
    }

    public static void dispose()
    {
        icons.dispose();
        icons_inverted.dispose();
        icons_overlay.dispose();
        icons_backdrop.dispose();
    }

    public static TextureSheet getSheet()
    {
        return icons;
    }

    public static void draw(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        icons.drawIndex(batch, index, x, y, width, height);
    }

    public static void drawInverted(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        icons_inverted.drawIndex(batch, index, x, y, width, height);
    }

    public static void drawBackdrop(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        icons_backdrop.drawIndex(batch, index, x, y, width, height);
    }

    public static void drawSolid2(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        icons_overlay.drawIndex(batch, index, x, y, width, height);
    }

    public static void drawSquare(SpriteBatch batch, float x, float y, float width, float height)
    {
        icons.drawIndex(batch, 14, x, y, width, height);
    }

}
