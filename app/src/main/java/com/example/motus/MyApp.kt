package com.example.motus

import android.app.Application
import android.util.Log
import com.example.data.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Log MyApp", "onCreate called")
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}