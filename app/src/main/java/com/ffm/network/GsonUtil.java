package com.ffm.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class GsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    public static Gson getGson() {
        return gson;
    }
}
