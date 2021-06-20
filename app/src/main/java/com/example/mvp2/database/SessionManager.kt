package com.example.mvp2.database

import android.content.Context
import android.content.SharedPreferences
import com.example.mvp2.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN  = "user_token"
        const val USER_FNAME  = "user_fname"
        const val USER_LNAME  = "user_lname"
        const val USER_EMAIL  = "user_email"
        const val USER_PHONE  = "user_phone"
        const val USER_ROLE   = "user_role"
        const val USER_PHOTO  = "user_photo"
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

    fun saveUserFName(fname: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_FNAME, fname).commit()
    }

    fun removeUserFName(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_FNAME).commit()
    }

    fun fetchUserFName(): String? {
        return prefs.getString(USER_FNAME, null)
    }

    /////////////////////

    fun saveUserLName(lname: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_LNAME, lname).commit()
    }

    fun removeUserLName(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_LNAME).commit()
    }

    fun fetchUserLName(): String? {
        return prefs.getString(USER_LNAME, null)
    }

    /////////////////////

    fun saveUserEmail(email: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_EMAIL, email).commit()
    }

    fun removeUserEmail(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_EMAIL).commit()
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    /////////////////////

    fun saveUserPhone(phone: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_PHONE, phone).commit()
    }

    fun removeUserPhone(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_PHONE).commit()
    }

    fun fetchUserPhone(): String? {
        return prefs.getString(USER_PHONE, null)
    }

    /////////////////////

    fun saveUserRole(role: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_ROLE, role).commit()
    }

    fun removeUserRole(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_ROLE).commit()
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    /////////////////////

    fun saveUserPhoto(photo: String): Boolean {
        val editor = prefs.edit()
        return editor.putString(USER_PHOTO, photo).commit()
    }

    fun removeUserPhoto(): Boolean {
        val editor = prefs.edit()
        return editor.remove(USER_PHOTO).commit()
    }

    fun fetchUserPhoto(): String? {
        return prefs.getString(USER_PHOTO, null)
    }
}