package com.lucascamilo.fitstrike.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.lucascamilo.fitstrike.domain.model.User

class SecurePreferences(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUser(user: User) {
        sharedPreferences.edit().apply {
            putString("user_name", user.name)
            putString("access_token", user.token.accessToken)
            putString("refresh_token", user.token.refreshToken)
            apply()
        }
    }

    fun getUserName(): String? = sharedPreferences.getString("user_name", null)
    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)
    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)
}