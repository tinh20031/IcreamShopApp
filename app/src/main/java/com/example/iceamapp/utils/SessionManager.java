package com.example.iceamapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserId(String userId) {
        editor.putString("USER_ID", userId);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString("USER_ID", null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
