package com.icurety;

public abstract class AdManager {

    public ClickToWin ctw;

    public AdManager(ClickToWin ctw)
    {
        this.ctw = ctw;
    }

    public abstract void load();
    public abstract void show();


}
