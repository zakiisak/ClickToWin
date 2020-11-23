package com.icurety;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontLoader {

    public static BitmapFont loadFont(String path, int size)
    {
        FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;

        return s.generateFont(params);
    }

    public static BitmapFont loadFontWithShadow(String path, int size, Color shadowColor, int borderSize)
    {
        FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        params.shadowColor = shadowColor;
        params.borderWidth = borderSize;

        return s.generateFont(params);
    }


}
