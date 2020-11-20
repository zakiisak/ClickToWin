package com.icurety;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class ClickToWin extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	BitmapFont font;
	OrthographicCamera camera;

	TextureSheet icons;

	@Override
	public void create () {
		FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 60;
		font = s.generateFont(params);
		icons = new TextureSheet(Gdx.files.internal("res/icons.png"), 32);





		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	int x, y;

	@Override
	public void render () {
		//batch.getTransformMatrix().rotate(new Vector3(0, 0, 1), 1);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, Gdx.graphics.getWidth() / 2 - img.getWidth() / 2, Gdx.graphics.getHeight() / 2 - img.getHeight() / 2);
		batch.draw(img, x++, y++);
		font.draw(batch, "" + Gdx.input.getRotation(), 0, 0);
		for(int i = 0; i < 20; i++)
		icons.drawIndex(batch, 16 * 16 + 5 + i, 30 + i * 256, 30, 256, 256);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
