package com.sealed.app.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.sealed.app.screen.main.MainScreen
import com.sealed.ui.them.Them

@ExperimentalFoundationApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Them {
                MainScreen()
            }
        }
    }

}