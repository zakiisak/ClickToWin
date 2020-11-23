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

import sun.rmi.runtime.Log;

public class ClickToWin extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;

	private BitmapFont.BitmapFontData fontData;
	private BitmapFont font;
	private AdManager adManager;
	private boolean paused = false;
	private int enemyIndex = 0;
	private Enemy currentEnemy;
	private BigInteger damage = BigInteger.valueOf(5);
	protected List<Entity> entities = new ArrayList<Entity>();
	private SaveSystem saveSystem;

	public ClickToWin(SaveSystem saveSystem, AdManager adManager)
	{
		this.saveSystem = saveSystem;
		this.adManager = adManager;
	}

	private void createGraphics()
	{
		FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 192;
		fontData = s.generateData(params);

		font = s.generateFont(params);
		Icons.init();


		batch = new SpriteBatch();
		background = new Texture("res/starBackground.jpeg");
	}

	private void disposeGraphics()
	{
		background.dispose();
		font.dispose();
		batch.dispose();
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(new Input(this));

		createGraphics();
		loadSave();

		System.out.println("create");
	}

	@Override
	public void resume() {
		super.resume();
		System.out.println("resume");
		createGraphics();
	}

	private void loadSave()
	{
		if(saveSystem.keyExists(SaveSystem.KEY_DAMAGE))
		{
			damage = saveSystem.getBigInt(SaveSystem.KEY_DAMAGE);
		}
		if(saveSystem.keyExists(SaveSystem.KEY_ENEMY))
		{
			currentEnemy = saveSystem.getFromJson(SaveSystem.KEY_ENEMY, Enemy.class).cpyWithCurrentHp();
		}
		else {
			currentEnemy = Enemy.ENEMIES.get(0).cpy();
		}
	}

	public void updateDamage(BigInteger newDamage)
	{
		damage = newDamage;
		saveSystem.save(SaveSystem.KEY_DAMAGE, damage);
	}

	public BigInteger getDamage()
	{
		return damage;
	}

	private void updateEnemy()
	{
		currentEnemy = null;
		enemyIndex++;
		if (enemyIndex >= Enemy.ENEMIES.size())
			enemyIndex = 0;
		currentEnemy = Enemy.ENEMIES.get(enemyIndex).cpy();

		//save
		saveSystem.save(SaveSystem.KEY_ENEMY, currentEnemy);
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
					updateEnemy();
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


			batch.setColor(1.0f, 0.8f, 0.2f, 1);
			String text = "" + (enemyIndex + 1);
			float textWidth = 0;
			for(int i = 0; i < text.length(); i++)
				textWidth += fontData.getGlyph(text.charAt(i)).xadvance;
			font.draw(batch, text, Gdx.graphics.getWidth() / 2 - textWidth / 2,
					currentEnemy.getY() + Enemy.ENEMY_SIZE + 100 + fontData.lineHeight);

			batch.end();
		}
	}

	@Override
	public void dispose () {
		disposeGraphics();
		System.out.println("dispose");
	}

	@Override
	public void pause() {
		super.pause();
		disposeGraphics();
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

	public void pauseGame()
	{
		paused = true;
		System.out.println("pause");
	}

	public void unpause()
	{
		paused = false;
	}
}
