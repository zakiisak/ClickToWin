package com.icurety;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextureSheet {

    private final Pixmap map;
    private final Texture sheet;
    public int indexSize;
    private final int indexLineWidth;

    public TextureSheet(FileHandle path, int indexSize)
    {
        map = new Pixmap(path);
        sheet = new Texture(map);
        sheet.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.indexSize = indexSize;
        indexLineWidth = sheet.getWidth() / indexSize;
    }

    public void drawIndex(SpriteBatch batch, int index, float x, float y, float width, float height)
    {
        final float sWidth = sheet.getWidth();
        final float sHeight = sheet.getHeight();
        final float uniformX = 1.0f / sWidth;
        final float uniformY = 1.0f / sHeight;
        final float u = uniformX * (float) (index % indexLineWidth * indexSize);
        final float v = uniformY * (float) (index / indexLineWidth * indexSize);

        final float u2 = u + uniformX * indexSize;
        final float v2 = v + uniformY * indexSize;

        batch.draw(sheet, x, y, width, height, u, v2, u2, v);
    }

    public Texture getTexture() {
        return sheet;
    }
    public Pixmap getPixMap() {
        return map;
    }

    public int getPixel(int iconIndex, int x, int y)
    {
        int startX = iconIndex % indexLineWidth * indexSize;
        int startY = iconIndex / indexLineWidth * indexSize;
        return map.getPixel(startX + x, startY + y);
    }

    public int getTextureCount()
    {
        return sheet.getWidth() / indexSize * sheet.getHeight() / indexSize;
    }
}
