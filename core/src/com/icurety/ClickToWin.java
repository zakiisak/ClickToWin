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
	private Texture background_stars, background_lava;
	private TextureSheet explosion3, explosion4;

	private BitmapFont.BitmapFontData fontData;
	private BitmapFont.BitmapFontData dpsFontData;
	private BitmapFont font, damageFont, dpsFont;
	private AdManager adManager;
	private boolean paused = false;
	private int enemyIndex = 0;
	private Enemy currentEnemy;
	private BigInteger damage = new BigInteger("5");
	protected List<Entity> entities = new ArrayList<Entity>();
	private SaveSystem saveSystem;
	private SoundSystem soundSystem;

	private boolean createdGraphics = false;

	private BigInteger clickCount = BigInteger.ZERO;
	private Runnable onWinAction;

	//dps varaibles
	private BigInteger tps = BigInteger.ZERO;
	private BigInteger tpsCounter = BigInteger.ZERO;
	private long tpsLastSecond;

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
		background_stars = new Texture("res/starBackground.jpeg");
		background_lava = new Texture("res/lava.jpg");
		explosion3 = new TextureSheet(Gdx.files.internal("res/explosion 3.png"),512);
		explosion4 = new TextureSheet(Gdx.files.internal("res/explosion 4.png"), 512);
		createdGraphics = true;
	}

	private void disposeGraphics()
	{
		if(createdGraphics) {
			background_stars.dispose();
			background_lava.dispose();
			font.dispose();
			damageFont.dispose();
			dpsFont.dispose();
			batch.dispose();
			Icons.dispose();
			createdGraphics = false;
		}
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
			currentEnemy = saveSystem.getEnemy(SaveSystem.KEY_ENEMY);
			enemyIndex = SaveSystem.getEnemyOutIndex;
		}
		else {
			currentEnemy = Enemy.get(enemyIndex/*Defaults to 0*/).cpy();
		}
		if(saveSystem.keyExists(SaveSystem.KEY_ITEM_MEDAL))
		{
			entities.add(new ItemPopup(this, ItemPopup.ITEM_GOLD_MEDAL, true));
		}
		if(saveSystem.keyExists(SaveSystem.KEY_ITEM_SUN))
		{
			entities.add(new ItemPopup(this, ItemPopup.ITEM_SUN, true));
		}
	}

	private void saveDamage() { saveSystem.save(SaveSystem.KEY_DAMAGE, damage); }
	private void saveEnemy() { saveSystem.saveEnemy(SaveSystem.KEY_ENEMY, enemyIndex, currentEnemy); }
	private void saveClickCount() {saveSystem.save(SaveSystem.KEY_CLICK_COUNT, clickCount);}

	//these two are run from the enemy class
	public void saveGoldMedal() {saveSystem.save(SaveSystem.KEY_ITEM_MEDAL, true);}
	public void saveSun() {saveSystem.save(SaveSystem.KEY_ITEM_SUN, true);}

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

	public void win()
	{
		onWinAction.run();
	}

	public void updateEnemy()
	{
		enemyIndex++;
		currentEnemy = Enemy.get(enemyIndex);
		saveEnemy();
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
		if(System.currentTimeMillis() - tpsLastSecond > 1000)
		{
			tpsLastSecond = System.currentTimeMillis();
			tps = tpsCounter;
			tpsCounter = BigInteger.ZERO;
		}

		//batch.getTransformMatrix().rotate(new Vector3(0, 0, 1), 1);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(paused == false) {
			batch.begin();
			batch.draw(enemyIndex % Enemy.getEnemyCount() >= 94 ? background_lava : background_stars, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.setColor(Color.WHITE);
			//batch.draw(img, Gdx.graphics.getWidth() / 2 - img.getWidth() / 2, Gdx.graphics.getHeight() / 2 - img.getHeight() / 2);

			if (currentEnemy != null) {
				currentEnemy.tickAndRender(batch);
				if (currentEnemy.dead) {
					Runnable postExplosionUpdateEnemy = null;
					if(currentEnemy.hasDeathEvent())
					{
						currentEnemy.callDeathEvent(this);
					}
					else
					{
						postExplosionUpdateEnemy = new Runnable() {
							@Override
							public void run() {
								updateEnemy();
							}
						};
					}
					currentEnemy = null;
					createExplosion(postExplosionUpdateEnemy);
				}
			}

			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tickAndRender(this, batch);
				if (e.dead) {
					e.onDeath(this);
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
			String dpsString = "tps: " + formatBigInteger(tps);
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
	}

	public void spawnPowerUp()
	{
		entities.add(new PowerUp(this));
	}

	private void spawnRingsOld(float screenX, float screenY)
	{

		int count1 = Math.min(Math.abs(damage.intValue()), 360);
		float degrees = (float) (Math.random() * 360D);
		final float dampFactor = 0.70f;
		final float speed = 100.0f;
		System.out.println("Count1: " + count1);
		for(int i = 0; i < count1; i++)
		{
			degrees += (360f / (float) count1);
			System.out.println(degrees);
			final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed);
			final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed);
			float rnd = (float) Math.random();
			float val = 0.86f + rnd * 0.14f;
			entities.add(new Particle(screenX, screenY, new Color(val, val, val, 1), dx, dy, dampFactor));
		}
		degrees %= 360;
		int count2 = Math.min(Math.abs(damage.divide(BigInteger.valueOf(1000)).intValue()), 360);
		if(count2 > 0) {
			degrees = (float) (Math.random() * 360D);
			if (count2 >= 0 && count2 < 5) count2 = 5;

			for (int i = 0; i < count2; i++) {
				degrees += (360f / (float) count2);
				final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.8f);
				final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.8f);
				entities.add(new Particle(screenX, screenY, new Color(0.5f, 0.5f, 1f, 1), dx, dy, dampFactor));
			}
			degrees %= 360;

			int count3 = Math.min(Math.abs(damage.divide(new BigInteger("1000000")).intValue()), 360);
			if(count3 > 0) {
				degrees = (float) (Math.random() * 360D);
				if (count3 >= 0 && count3 < 5) count3 = 5;

				for (int i = 0; i < count3; i++) {
					degrees += (360f / (float) count3);
					final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.6f);
					final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.6f);
					entities.add(new Particle(screenX, screenY, new Color(0.5f, 1, 0.5f, 1), dx, dy, dampFactor));
				}
				degrees %= 360;


				int count4 = Math.min(Math.abs(damage.divide(new BigInteger("1000000000")).intValue()), 360);
				if(count4 > 0) {
					degrees = (float) (Math.random() * 360D);
					if (count4 > 0 && count4 < 5) count4 = 5;

					for (int i = 0; i < count4; i++) {
						degrees += (360f / (float) count4);
						final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 0.4f);
						final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 0.4f);
						entities.add(new Particle(screenX, screenY, new Color(1f, 0.5f, 0.5f, 1), dx, dy, dampFactor));
					}
					degrees %= 360;


					int count6 = Math.min(Math.abs(damage.divide(new BigInteger("1000000000000000")).intValue()), 360);
					if(count6 > 0) {
						degrees = (float) (Math.random() * 360D);
						if (count6 >= 0 && count6 < 5) count6 = 5;

						for (int i = 0; i < count6; i++) {
							degrees += (360f / (float) count6);
							final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed * 1.5f);
							final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed * 1.5f);
							entities.add(new Particle(screenX, screenY, new Color(1, 1, 1, 1).fromHsv(degrees % 360, 0.75f, 1.0f), dx, dy, dampFactor));
						}
					}
				}
			}
		}
	}

	private void spawnRings(float screenX, float screenY)
	{
		int count1 = Math.min(Math.abs(damage.intValue()), 360);
		float degrees = (float) (Math.random() * 360D);
		final float dampFactor = 0.70f;
		final float speed = 100.0f;
		for(int i = 0; i < count1; i++)
		{
			degrees += (360f / (float) count1);
			final float dx = (float) (Math.cos(Math.toRadians(degrees)) * speed);
			final float dy = (float) (Math.sin(Math.toRadians(degrees)) * speed);
			float rnd = (float) Math.random();
			float val = 0.86f + rnd * 0.14f;
			entities.add(new Particle(screenX, screenY, new Color(val, val, val, 1), dx, dy, dampFactor));
		}
	}

	public void onTouch(float screenX, float screenY)
	{
		screenY = Gdx.graphics.getHeight() - screenY;

		if(currentEnemy != null)
		{
			//Better to just have him press on any point on the screen
			//if(currentEnemy.checkClick(screenX, screenY))
			//{
				currentEnemy.damage(damage);
				tpsCounter = tpsCounter.add(BigInteger.ONE);
				soundSystem.playDamage(currentEnemy.getHpPercentage());
				entities.add(new DamageNumber(damage, damageFont, screenX, screenY));
				clickCount = clickCount.add(BigInteger.ONE);
				//saveSystem.save(SaveSystem.KEY_CLICK_COUNT, clickCount);

				//spawn particles
				spawnRings(screenX, screenY);
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
		if(number.compareTo(new BigInteger("10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "G";
		}
		else if(number.compareTo(new BigInteger("1000000000000000000000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000000000000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "D";
		}
		else if(number.compareTo(new BigInteger("1000000000000000000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000000000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "N";
		}
		else if(number.compareTo(new BigInteger("1000000000000000000000000000")) > 0)
		{
			return decimal.divide(new BigDecimal("1000000000000000000000000000"), 3, RoundingMode.CEILING).stripTrailingZeros().toPlainString() + "O";
		}
		else if(number.compareTo(new BigInteger("1000000000000000000000")) > 0)
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

	public void startOver()
	{
		updateEnemy();
	}

	public SoundSystem getSoundSystem()
	{
		return soundSystem;
	}
}
