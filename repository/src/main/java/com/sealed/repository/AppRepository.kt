package com.sealed.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.sealed.repository.model.AppModel
import kotlinx.coroutines.flow.flow

class AppRepository {

    fun vncUrl(context: Context) = context
        .getSharedPreferences("sealed", Context.MODE_PRIVATE)
        .getString("vncUrl", "192.168.8.162")!!

    @SuppressLint("CommitPrefEdits")
    fun changeVncUrl(context: Context, url: String) = context
        .getSharedPreferences("sealed", Context.MODE_PRIVATE)
        .edit()
        .putString("vncUrl", url)
        .commit()

    val apps = flow {
        emit(arrayListOf(
            AppModel(
                name = "CNN",
                isBookmark = false,
                url = "",
                icon = R.drawable.logo_app_cnn
            ),

            AppModel(
                name = "CPUZ",
                isBookmark = false,
                url = "",
                icon = R.drawable.logo_app_cpuz
            ),

            AppModel(
                name = "DevChek",
                isBookmark = false,
                url = "",
                icon = R.drawable.logo_app_dev_chek
            ),

            AppModel(
                name = "CutTheRope",
                isBookmark = false,
                url = "",
                icon = R.drawable.logo_app_cut_the_rope
            ),

            AppModel(
                name = "Facebook",
                isBookmark = false,
                url = "",
                icon = R.drawable.logo_app_fb
            ),

            AppModel(
                name = "DeviceID",
                isBookmark = true,
                url = "",
                icon = R.drawable.logo_app_device_id
            ),

            AppModel(
                name = "Chrome",
                isBookmark = true,
                url = "",
                icon = R.drawable.logo_app_chrome
            ),

            AppModel(
                name = "Files",
                isBookmark = true,
                url = "",
                icon = R.drawable.logo_app_files
            ),

            AppModel(
                name = "Play market",
                isBookmark = true,
                url = "",
                icon = R.drawable.logo_app_files
            )
        ))
    }

}