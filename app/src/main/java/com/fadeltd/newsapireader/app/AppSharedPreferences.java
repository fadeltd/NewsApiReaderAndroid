package com.fadeltd.newsapireader.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {
    private Context context;
    private static final String PREFS_NAME = "NEWS_READER";

    public AppSharedPreferences(Context context) {
        this.context = context;
    }

    public void setData(String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value).apply();
    }

    public void deleteData(String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(key).apply();
    }

    public boolean getData(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }
}
