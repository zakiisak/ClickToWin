package com.icurety;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ClickToWin extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, background;


	BitmapFont font;
	OrthographicCamera camera;

	private AdManager adManager;
	private boolean paused = false;

	private int enemyIndex = 0;
	Enemy currentEnemy;
	public BigInteger damage = BigInteger.valueOf(5);

	protected List<Entity> entities = new ArrayList<Entity>();

	public ClickToWin(AdManager adManager)
	{
		this.adManager = adManager;
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new Input(this));
		FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 120;
		font = s.generateFont(params);
		Icons.init();


		currentEnemy = Enemy.ENEMIES.get(0).cpy();

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
		if(paused == false) {
			batch.begin();
			batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
			batch.setColor(Color.WHITE);
			//batch.draw(img, Gdx.graphics.getWidth() / 2 - img.getWidth() / 2, Gdx.graphics.getHeight() / 2 - img.getHeight() / 2);

			if (currentEnemy != null) {
				currentEnemy.tickAndRender(batch);
				if (currentEnemy.dead) {
					currentEnemy = null;
					enemyIndex++;
					if (enemyIndex >= Enemy.ENEMIES.size())
						enemyIndex = 0;
					currentEnemy = Enemy.ENEMIES.get(enemyIndex).cpy();
				}
			}

			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tickAndRender(this, batch);
				if (e.dead) {
					entities.remove(i--);
				}
			}
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).renderPost( batch);
			}
			batch.end();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void spawnPowerUp()
	{
		entities.add(new PowerUp(this));
	}

	public void onTouch(float screenX, float screenY)
	{
		screenY = Gdx.graphics.getHeight() - screenY;
		System.out.println("screenTouch");


		if(currentEnemy != null)
		{
			if(currentEnemy.checkClick(screenX, screenY))
			{
				currentEnemy.damage(damage);


				//spawn particles

				int count = Math.min(damage.intValue(), 360);
				float degrees = (float) (Math.random() * 360D);
				final float dampFactor = 0.70f;
				final float speed = 100.0f;
				for(int i = 0; i < count; i++)
				{
					degrees += (360 / count) % 360;
					final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed);
					final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed);
					float rnd = (float) Math.random();
					float val = 0.86f + rnd * 0.14f;
					entities.add(new Particle(screenX, screenY, new Color(val, val, val, 1), dx, dy, dampFactor));
				}
			}
		}
	}

	public void pause()
	{
		paused = true;
	}

	public void unpause()
	{
		paused = false;
	}
}
