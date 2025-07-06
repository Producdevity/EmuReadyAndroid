package com.emuready.emuready

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EmuReadyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Application initialization
        // Note: Authentication is handled through the API endpoints
        // using the EmuReady tRPC auth endpoints
    }
}