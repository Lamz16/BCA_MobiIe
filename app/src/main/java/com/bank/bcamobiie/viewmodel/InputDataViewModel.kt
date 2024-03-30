package com.bank.bcamobiie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bank.bcamobiie.data.DataRepository
import com.bank.bcamobiie.datastore.Userdata
import kotlinx.coroutines.launch

class InputDataViewModel (private val repository: DataRepository) : ViewModel(){

    fun saveSession(user: Userdata) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession() = repository.getSession().asLiveData()

}