package com.icurety;

import com.badlogic.gdx.utils.Json;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class SaveSystem {
    private Json json = new Json();
    public static final String KEY_DAMAGE = "damage";
    public static final String KEY_ENEMY = "enemy";

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

    public <T> T getFromJson(String key, Class<T> type)
    {
        return json.fromJson(type, getString(key));
    }

}
