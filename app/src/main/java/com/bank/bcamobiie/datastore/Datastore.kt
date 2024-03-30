package com.bank.bcamobiie.datastore

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference (private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: Userdata) {
        dataStore.edit { preferences ->
            preferences[NOREK_KEY] = user.idKartu
            preferences[PIN_KEY] = user.pin
            preferences[IS_LOGIN_KEY] = true
            Log.d(TAG, "saveSession: norek is ${user.idKartu} ")
            Log.d(TAG, "saveSession: pin is ${user.pin} ")
            Log.d(TAG, "saveSession: login is ${user.isLogin} ")
        }

    }

    fun getSession(): Flow<Userdata> {
        return dataStore.data.map { preferences ->
            Userdata(
                preferences[NOREK_KEY] ?: "",
                preferences[PIN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }


    companion object {
        private val NOREK_KEY = stringPreferencesKey("norek")
        private val PIN_KEY = stringPreferencesKey("pin")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

    }
}