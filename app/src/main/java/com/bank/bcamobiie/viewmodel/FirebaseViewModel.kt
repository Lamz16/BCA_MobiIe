package com.bank.bcamobiie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bank.bcamobiie.data.FirebaseDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseDataViewModel : ViewModel() {

    private val repository = FirebaseDataRepository()

    fun fetchUserData(idKartu: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchUserData(idKartu)
        }
    }

    fun getUserData(): LiveData<String> {

        return repository.userData
    }

    fun fetchSaldo(idKartu: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchSaldo(idKartu)
        }
    }

    fun fetchRekening(idKartu: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRekening(idKartu)
        }
    }

    fun getSaldo(): LiveData<String> {
        return repository.saldo
    }

    fun getRekening(): LiveData<String> {
        return repository.rekening
    }


}
