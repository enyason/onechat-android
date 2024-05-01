package com.enyason.onechat.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.enyason.onechat.data.remote.models.Token
import com.enyason.onechat.data.remote.models.User
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationDataStore @Inject constructor(
    private val sharedPreferences: SharedPreferences, private val gson: Gson
) {


    fun saveUser(user: User) {
        sharedPreferences.edit {
            val gsonString = gson.toJson(user, User::class.java)
            putString(KEY_USER, gsonString)
        }
    }

    fun getUser(): User? {
        val jsonString = sharedPreferences.getString(KEY_USER, null)
        return gson.fromJson(jsonString, User::class.java)
    }

    fun saveToken(user: Token) {
        sharedPreferences.edit {
            val gsonString = gson.toJson(user, Token::class.java)
            putString(KEY_TOKEN, gsonString)
        }
    }

    fun getToken(): Token? {
        val jsonString = sharedPreferences.getString(KEY_TOKEN, null)
        return gson.fromJson(jsonString, Token::class.java)
    }


    companion object {
        private const val KEY_USER = "key_user"
        private const val KEY_TOKEN = "key_token"
    }
}
