package com.example.data

import com.example.data.api.RetrofitClient
import com.example.data.database.Database
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    // Création du client Retrofit et de l'API service
    single { RetrofitClient.create() }

    // Création de la base de données et du DAO
    single { Database.getInstance(androidContext()) }
    single { get<Database>().wordDao() }
    single { get<Database>().gameDao() }
}



