package com.bank.bcamobiie.data

import com.bank.bcamobiie.datastore.UserPreference
import com.bank.bcamobiie.datastore.Userdata
import kotlinx.coroutines.flow.Flow

class DataRepository(private val userPreference: UserPreference) {

    suspend fun saveSession(user: Userdata) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<Userdata> {
        return userPreference.getSession()
    }


}