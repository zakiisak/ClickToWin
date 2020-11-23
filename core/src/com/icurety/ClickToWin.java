package com.icurety;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import sun.rmi.runtime.Log;

public class ClickToWin extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private TextureSheet explosion3, explosion4;

	private BitmapFont.BitmapFontData fontData;
	private BitmapFont.BitmapFontData dpsFontData;
	private BitmapFont font, damageFont, dpsFont;
	private AdManager adManager;
	private boolean paused = false;
	private int enemyIndex = 0;
	private Enemy currentEnemy;
	private BigInteger damage = new BigInteger("12345678931234567890");
	protected List<Entity> entities = new ArrayList<Entity>();
	private SaveSystem saveSystem;
	private SoundSystem soundSystem;

	private BigInteger clickCount = BigInteger.ZERO;
	private Runnable onWinAction;

	//dps varaibles
	private BigInteger dps = BigInteger.ZERO;
	private BigInteger dpsCounter = BigInteger.ZERO;
	private long dpsLastSecond;

	public ClickToWin(SaveSystem saveSystem, AdManager adManager, Runnable onWinAction)
	{
		this.saveSystem = saveSystem;
		this.adManager = adManager;
		this.onWinAction = onWinAction;
	}

	private void loadFonts()
	{
		FreeTypeFontGenerator.setMaxTextureSize(2048);
		FreeTypeFontGenerator s = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 192;
		fontData = s.generateData(params);
		font = s.generateFont(params);
		damageFont = FontLoader.loadFontWithShadow("Yorktown.ttf", 100, Color.BLACK, 2);

		FreeTypeFontGenerator dpsGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Modak-Regular.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter dpsParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
		dpsParams.size = 50;
		dpsFontData = dpsGenerator.generateData(dpsParams);
		dpsFont = dpsGenerator.generateFont(dpsParams);
	}

	private void createGraphics()
	{
		loadFonts();
		Icons.init();
		soundSystem = new SoundSystem();
		soundSystem.init();

		batch = new SpriteBatch();
		background = new Texture("res/starBackground.jpeg");
		explosion3 = new TextureSheet(Gdx.files.internal("res/explosion 3.png"),512);
		explosion4 = new TextureSheet(Gdx.files.internal("res/explosion 4.png"), 512);
	}

	private void disposeGraphics()
	{
		background.dispose();
		font.dispose();
		damageFont.dispose();
		dpsFont.dispose();
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
		if(saveSystem.keyExists(SaveSystem.KEY_CLICK_COUNT))
		{
			clickCount = saveSystem.getBigInt(SaveSystem.KEY_CLICK_COUNT);
		}
		if(saveSystem.keyExists(SaveSystem.KEY_ENEMY))
		{
			currentEnemy = saveSystem.getFromJson(SaveSystem.KEY_ENEMY, Enemy.class).cpyWithCurrentHp();
		}
		else {
			currentEnemy = Enemy.ENEMIES.get(0).cpy();
		}
	}

	private void saveDamage() { saveSystem.save(SaveSystem.KEY_DAMAGE, damage); }
	private void saveEnemy() { saveSystem.save(SaveSystem.KEY_ENEMY, currentEnemy); }
	private void saveClickCount() {saveSystem.save(SaveSystem.KEY_CLICK_COUNT, clickCount);}
	private void save()
	{
		saveDamage();
		saveEnemy();
		saveClickCount();
	}


	public void updateDamage(BigInteger newDamage)
	{
		damage = newDamage;
		saveDamage();
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
		{
			onWinAction.run();
		}
		else {
			currentEnemy = Enemy.ENEMIES.get(enemyIndex).cpy();
			//save
			saveEnemy();
		}
	}

	private void createExplosion(Runnable onFinished)
	{
		soundSystem.playExplosion();
		//entities.add(new Flash(Color.WHITE, 60));
		Explosion explosion = new Explosion(Math.random() > 0.5 ? explosion3 : explosion4, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		explosion.setDeathCallback(onFinished);
		entities.add(explosion);
	}

	int x, y;

	@Override
	public void render () {
		//update dpsCounter
		if(System.currentTimeMillis() - dpsLastSecond > 1000)
		{
			dpsLastSecond = System.currentTimeMillis();
			dps = dpsCounter;
			dpsCounter = BigInteger.ZERO;
		}

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
					createExplosion(new Runnable() {
						@Override
						public void run() {
							updateEnemy();
						}
					});
				}
			}

			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tickAndRender(this, batch);
				if (e.dead) {
					if(e.deathCallback != null)
						e.deathCallback.run();
					entities.remove(i--);
				}
			}
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).renderPost( batch);
			}


			String text = "" + (enemyIndex + 1);
			float textWidth = 0;
			for(int i = 0; i < text.length(); i++)
				textWidth += fontData.getGlyph(text.charAt(i)).xadvance;
			float x = Gdx.graphics.getWidth() / 2 - textWidth / 2;
			float y = Gdx.graphics.getHeight() - fontData.lineHeight - 100; //currentEnemy.getY() + Enemy.ENEMY_SIZE + 200 + fontData.lineHeight;
			font.setColor(0, 0, 0, 1);
			font.draw(batch, text, x - 2, y);
			font.draw(batch, text, x + 2, y);
			font.draw(batch, text, x, y - 2);
			font.draw(batch, text, x, y + 2);
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(batch, text, x, y);


			//draw dps
			float dpsStringWidth = 0;
			String dpsString = "dps: " + formatBigInteger(dps);
			dpsFont.setColor(1,1, 1, 1);
			for(int i = 0; i < dpsString.length(); i++)
				dpsStringWidth += dpsFontData.getGlyph(dpsString.charAt(i)).xadvance;
			dpsFont.draw(batch, dpsString, Gdx.graphics.getWidth() - 30 - dpsStringWidth, Gdx.graphics.getHeight() - dpsFontData.lineHeight);
			batch.end();
		}
	}

	@Override
	public void dispose () {
		disposeGraphics();
		System.out.println("dispose");
		save();
	}

	@Override
	public void pause() {
		super.pause();
		disposeGraphics();
		save();
	}

	public void spawnPowerUp()
	{
		entities.add(new PowerUp(this));
	}

	public void onTouch(float screenX, float screenY)
	{
		screenY = Gdx.graphics.getHeight() - screenY;
		System.out.println("screenTouch");
		onWinAction.run();

		if(currentEnemy != null)
		{
			//Better to just have him press on any point on the screen
			//if(currentEnemy.checkClick(screenX, screenY))
			//{
				currentEnemy.damage(damage);
				dpsCounter = dpsCounter.add(damage);
				soundSystem.playDamage(currentEnemy.getHpPercentage());
				entities.add(new DamageNumber(damage, damageFont, screenX, screenY));
				clickCount = clickCount.add(BigInteger.ONE);
				saveSystem.save(SaveSystem.KEY_CLICK_COUNT, clickCount);

				//spawn particles

				int count1 = Math.min(Math.abs(damage.intValue()), 360);
				float degrees = (float) (Math.random() * 360D);
				final float dampFactor = 0.70f;
				final float speed = 100.0f;
				for(int i = 0; i < count1; i++)
				{
					degrees += (360 / count1) % 360;
					final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed);
					final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed);
					float rnd = (float) Math.random();
					float val = 0.86f + rnd * 0.14f;
					entities.add(new Particle(screenX, screenY, new Color(val, val, val, 1), dx, dy, dampFactor));
				}
				int count2 = Math.min(Math.abs(damage.divide(BigInteger.valueOf(1000)).intValue()), 360);
				if(count2 > 0) {
					if (count2 >= 0 && count2 < 5) count2 = 5;

					for (int i = 0; i < count2; i++) {
						degrees += (360 / count2) % 360;
						final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.8f);
						final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.8f);
						entities.add(new Particle(screenX, screenY, new Color(0.5f, 0.5f, 1f, 1), dx, dy, dampFactor));
					}

					int count3 = Math.min(Math.abs(damage.divide(new BigInteger("1000000")).intValue()), 360);
					if(count3 > 0) {
						if (count3 >= 0 && count3 < 5) count3 = 5;

						for (int i = 0; i < count3; i++) {
							degrees += (360 / count3) % 360;
							final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.6f);
							final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.6f);
							entities.add(new Particle(screenX, screenY, new Color(0.5f, 1, 0.5f, 1), dx, dy, dampFactor));
						}


						int count4 = Math.min(Math.abs(damage.divide(new BigInteger("1000000000")).intValue()), 360);
						if(count4 > 0) {
							if (count4 > 0 && count4 < 5) count4 = 5;

							for (int i = 0; i < count4; i++) {
								degrees += (360 / count4) % 360;
								final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.4f);
								final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.4f);
								entities.add(new Particle(screenX, screenY, new Color(1f, 0.5f, 0.5f, 1), dx, dy, dampFactor));
							}


							int count6 = Math.min(Math.abs(damage.divide(new BigInteger("1000000000000000")).intValue()), 360);
							if(count6 > 0) {
								if (count6 >= 0 && count6 < 5) count6 = 5;

								for (int i = 0; i < count6; i++) {
									degrees += (360 / count6) % 360;
									final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 1.5f);
									final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 1.5f);
									entities.add(new Particle(screenX, screenY, new Color(1, 1, 1, 1).fromHsv(degrees % 360, 0.75f, 1.0f), dx, dy, dampFactor));
								}
							}
						}
					}
				}
			//}
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

	public static String formatBigInteger(BigInteger number)
	{
		BigDecimal decimal = new BigDecimal(number);
		String result = "";
		if(number.compareTo(new BigInteger("1000000000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "S";
		}
		else if(number.compareTo(new BigInteger("1000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "Q";
		}
		else if(number.compareTo(new BigInteger("1000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "T";
		}
		else if(number.compareTo(new BigInteger("1000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "B";
		}
		else if(number.compareTo(new BigInteger("1000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "M";
		}
		return number.toString();
	}
}
