package mg.jn.seralink.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "seralink_prefs")

class TokenDataStore(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    val token: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    val userRole: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_ROLE_KEY]
    }

    val userName: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME_KEY]
    }

    suspend fun saveToken(token: String, role: String, name: String, id: Int) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ROLE_KEY] = role
            prefs[USER_NAME_KEY] = name
            prefs[USER_ID_KEY] = id.toString()
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}