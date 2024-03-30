package com.bank.bcamobiie.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseDataRepository {

    private val _userData = MutableLiveData<String>()
    val userData: LiveData<String>
        get() = _userData

    private val _saldo = MutableLiveData<String>()
    val saldo: LiveData<String>
        get() = _saldo

    private val _rekening = MutableLiveData<String>()
    val rekening: LiveData<String>
        get() = _rekening


    fun fetchUserData(idKartu: String) {
        val databaseRef =
            FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference
        databaseRef.child("data_akun").orderByChild("idKartu").equalTo(idKartu)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (dataSnapshotChild in dataSnapshot.children) {
                            val namaNasabah =
                                dataSnapshotChild.child("nama").getValue(String::class.java)
                            _userData.postValue(namaNasabah!!)
                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle cancelled event
                }
            })
    }

    fun fetchSaldo(idKartu: String) {
        val databaseRef = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference
        databaseRef.child("data_akun").orderByChild("idKartu").equalTo(idKartu)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (dataSnapshotChild in dataSnapshot.children) {
                            val saldo = dataSnapshotChild.child("saldo").getValue(String::class.java)
                            _saldo.postValue(saldo!!)
                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle cancelled event
                }
            })
    }

    fun fetchRekening(idKartu: String) {
        val databaseRef = FirebaseDatabase.getInstance("https://bca-mobiile-default-rtdb.firebaseio.com/").reference
        databaseRef.child("data_rekening").orderByChild("idKartu").equalTo(idKartu)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (dataSnapshotChild in dataSnapshot.children) {
                            val rekening = dataSnapshotChild.child("idRekening").getValue(String::class.java)
                            _rekening.postValue(rekening!!)
                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle cancelled event
                }
            })
    }



}
