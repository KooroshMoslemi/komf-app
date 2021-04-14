package com.example.mvp2.database

import android.content.Context
import android.content.SharedPreferences
import com.example.mvp2.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val FNAME_TOKEN = "fname_token"
    }

    fun saveAuthToken(token: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_TOKEN, token).commit()
    }

    fun removeAuthToken(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_TOKEN).commit()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /////////////////////

    fun saveFNameToken(token: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(FNAME_TOKEN, token).commit()
    }

    fun removeFNameToken(): Boolean {
        val editor = prefs.edit()
        return editor.remove(FNAME_TOKEN).commit()
    }

    fun fetchFNameToken(): String? {
        return prefs.getString(FNAME_TOKEN, null)
    }
}