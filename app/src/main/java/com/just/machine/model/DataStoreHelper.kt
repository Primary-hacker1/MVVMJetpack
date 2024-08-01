package com.just.machine.model


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.just.machine.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreHelper(private val context: Context) {

    private val dataStore = context.dataStore

    // Key Definitions
    companion object {

        val USERID = intPreferencesKey("userId")
        val USERNAME = stringPreferencesKey("userName")
        val PASS = stringPreferencesKey("pass")
        val TOKEN = stringPreferencesKey("token")
        val TENANTID = stringPreferencesKey("tenantId")
        val REMEMBER_THE_PASSWORD = booleanPreferencesKey("rememberThePassword")

        private var INSTANCE: DataStoreHelper? = null

        fun getInstance(): DataStoreHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataStoreHelper(App.instance!!).also { INSTANCE = it }
            }
        }
    }

    val exampleUserId: Flow<Int> = dataStore.data
        .catch { exception ->
            // Handle exceptions
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USERID] ?: 0
        }

    val exampleUserName: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[USERNAME]
        }

    val examplePass: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PASS]
        }

    val exampleRememberThePassword: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[REMEMBER_THE_PASSWORD] ?: false
        }

    val exampleToken: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[TOKEN]
        }

    val exampleTenantId: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[TENANTID]
        }


    suspend fun saveUserId(value: Int) {
        dataStore.edit { preferences ->
            preferences[USERID] = value
        }
    }

    suspend fun saveUserName(value: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = value
        }
    }

    suspend fun savePass(value: String) {
        dataStore.edit { preferences ->
            preferences[PASS] = value
        }
    }

    suspend fun saveToken(value: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = value
        }
    }

    suspend fun saveTenantId(value: String) {
        dataStore.edit { preferences ->
            preferences[TENANTID] = value
        }
    }

    suspend fun saveRememberThePassword(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_THE_PASSWORD] = value
        }
    }
}
