package com.sealed.repository.model

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

data class AppModel(
    val name: String,
    val url: String,
    val isBookmark: Boolean,
    //url
    val icon: String
) {
    var iconBitmap: Bitmap? = null
}