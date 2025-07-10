package com.emuready.emuready

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EmuReadyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Clerk initialization disabled for now - will be enabled when auth is needed
        // Clerk.initialize(
        //     this,
        //     "pk_test_xxxx"
        // )
    }
}
