package com.example.hundredplaces

import android.app.Application
import com.example.hundredplaces.data.containers.DefaultAppContainer

class HundredPlacesApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: DefaultAppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}