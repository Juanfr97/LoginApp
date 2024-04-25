package com.example.loginapp

import android.app.Application
import com.example.loginapp.di.AppModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoginApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}