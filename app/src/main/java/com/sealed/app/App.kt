package com.sealed.app

import android.app.Application
import com.sealed.repository.Builder

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Builder.build(this)
    }

}