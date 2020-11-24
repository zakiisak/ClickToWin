package com.icurety;

import com.badlogic.gdx.utils.Json;

import java.io.InvalidObjectException;
import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class SaveSystem {
    private Json json = new Json();
    public static final String KEY_DAMAGE = "damage";
    public static final String KEY_ENEMY = "enemy";
    public static final String KEY_CLICK_COUNT = "clickCount";
    public static final String KEY_ITEM_MEDAL = "itemMedal";
    public static final String KEY_ITEM_SUN = "itemSun";

    public abstract void save(String key, Object value);
    public abstract String getString(String key);
    public abstract boolean keyExists(String key);
    public abstract float getFloat(String key);
    public abstract int getInt(String key);
    public abstract boolean getBoolean(String key);

    public BigInteger getBigInt(String key)
    {
        return new BigInteger(getString(key));
    }
    public BigDecimal getBigDecimal(String key)
    {
        return new BigDecimal(getString(key));
    }


    public void saveAsJson(String key, Object obj)
    {
        String str = json.toJson(obj);
        save(key, str);
    }

    public void saveEnemy(String key, int index, Enemy enemy)
    {
        String result = "";
        result += index + "@";
        result += enemy.getHp().toString();
        save(key, result);
    }


    protected static int getEnemyOutIndex;
    public Enemy getEnemy(String key)
    {
        String enemyString = getString(key);
        String[] splittedValues = enemyString.split("@");
        int index = Integer.parseInt(splittedValues[0]);
        BigInteger hp = new BigInteger(splittedValues[1]);
        getEnemyOutIndex = index;
        return Enemy.getWithHp(index, hp);
    }

    public <T> T getFromJson(String key, Class<T> type)
    {
        return json.fromJson(type, getString(key));
    }

}
