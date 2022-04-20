package com.sealed.repository

import android.content.Context

object Builder {

    internal lateinit var appContext: Context

    fun build(appContext: Context) {
        this.appContext = appContext
    }

}