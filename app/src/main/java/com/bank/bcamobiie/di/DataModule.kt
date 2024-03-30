package com.bank.bcamobiie.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bank.bcamobiie.data.DataRepository
import com.bank.bcamobiie.data.FirebaseDataRepository
import com.bank.bcamobiie.datastore.UserPreference
import com.bank.bcamobiie.datastore.dataStore
import com.bank.bcamobiie.viewmodel.FirebaseDataViewModel
import com.bank.bcamobiie.viewmodel.InputDataViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val dataModule = module {
    single { provideDataStore(androidContext()) }
    single {UserPreference(get())}
    single { DataRepository(get()) }
    viewModel { InputDataViewModel(get()) }
    single { FirebaseDataRepository() }
    viewModel { FirebaseDataViewModel() }
}

fun provideDataStore(context: Context): DataStore<Preferences> {
    return context.dataStore
}