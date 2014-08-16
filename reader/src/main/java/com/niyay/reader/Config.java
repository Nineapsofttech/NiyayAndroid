package com.niyay.reader;

import android.content.Context;

public class Config
{
    private static Config instance = null;

    public String user;
	public String password;
    public Integer category;

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }
}
