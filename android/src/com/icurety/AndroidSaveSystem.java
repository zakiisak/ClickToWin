package com.icurety;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AndroidSaveSystem extends SaveSystem {

    private SharedPreferences preferences;

    public AndroidSaveSystem(Activity activity)
    {
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public void save(String key, Object value) {
        SharedPreferences.Editor editor = preferences.edit();
        if(value instanceof Boolean)
        {
            editor.putBoolean(key, (boolean) value);
        }
        else if(value instanceof Integer)
        {
            editor.putInt(key, (int) value);
        }
        else if(value instanceof Long)
        {
            editor.putLong(key, (long) value);
        }
        else if(value instanceof Float)
        {
            editor.putFloat(key, (float) value);
        }
        else if(value instanceof String)
        {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

    @Override
    public String getString(String key) {
        return preferences.getString(key, null);
    }

    @Override
    public boolean keyExists(String key) {
        return preferences.contains(key);
    }

    @Override
    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    @Override
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    @Override
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }
}
