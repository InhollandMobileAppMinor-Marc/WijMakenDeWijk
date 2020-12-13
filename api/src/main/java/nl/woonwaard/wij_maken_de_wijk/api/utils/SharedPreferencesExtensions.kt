package nl.woonwaard.wij_maken_de_wijk.api.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

fun Context.getPreferences(name: String = this.packageName + "_preferences", encrypted: Boolean = true): SharedPreferences {
    return if(encrypted) EncryptedSharedPreferences.create(
        this.packageName + "_preferences",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        this,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ) else getSharedPreferences(name, Context.MODE_PRIVATE)
}

fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) = edit().apply(block).apply()
