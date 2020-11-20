package com.icurety;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ClickToWin extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, background;


	BitmapFont font;
	OrthographicCamera camera;

	TextureSheet icons;

	List<Particle> particles = new ArrayList<Particle>();
	Enemy currentEnemy;
	private BigInteger damage = BigInteger.valueOf(5);

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new Input(this));
		FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 120;
		font = s.generateFont(params);
		icons = new TextureSheet(Gdx.files.internal("res/icons.png"), 32);



		currentEnemy = Enemy.ENEMIES.get(0);

		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		background = new Texture("res/starBackground.jpeg");
	}

	int x, y;

	@Override
	public void render () {
		//batch.getTransformMatrix().rotate(new Vector3(0, 0, 1), 1);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
		batch.setColor(Color.WHITE);
		//batch.draw(img, Gdx.graphics.getWidth() / 2 - img.getWidth() / 2, Gdx.graphics.getHeight() / 2 - img.getHeight() / 2);

		if(currentEnemy != null)
		{
			currentEnemy.tickAndRender(batch, icons);
			if(currentEnemy.dead)
				currentEnemy = null;
		}

		for(int i = 0; i < particles.size(); i++)
		{
			Particle p = particles.get(i);
			p.tickAndRender(batch, icons);
			if(p.dead)
			{
				particles.remove(i--);
			}
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void onTouch(float screenX, float screenY)
	{
		screenY = Gdx.graphics.getHeight() - screenY;
		System.out.println("screenTouch");
		int count = 90 + (int) (Math.random() * 100D);
		final float dampFactor = 0.98f;
		final float maxSpeed = 5.0f;
		for(int i = 0; i < count; i++)
		{
			final float degrees = (float) Math.random() * 360;
			final float dx = (float) (Math.cos(Math.toRadians(degrees)) * Math.random() * maxSpeed);
			final float dy = (float) (Math.sin(Math.toRadians(degrees)) * Math.random() * maxSpeed);
			particles.add(new Particle(screenX, screenY, new Color(1.0f, 0.75f + (float) Math.random() * 0.25f, 0, 1), dx, dy, dampFactor));
		}

		if(currentEnemy != null)
		{
			if(currentEnemy.checkClick(screenX, screenY))
			{
				currentEnemy.damage(damage);
			}
		}
	}
}
