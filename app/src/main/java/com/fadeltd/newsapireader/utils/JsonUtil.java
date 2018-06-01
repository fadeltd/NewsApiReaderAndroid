package com.fadeltd.newsapireader.utils;

import com.google.gson.JsonObject;

public class JsonUtil {
    public static boolean jsonExist(JsonObject object, String key) {
        return object.has(key) && !object.get(key).isJsonNull();
    }

    public static String getString(JsonObject object, String key) {
        return jsonExist(object, key) ? object.get(key).getAsString() : null;
    }
}
