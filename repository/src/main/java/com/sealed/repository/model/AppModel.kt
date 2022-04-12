package com.sealed.repository.model

import androidx.annotation.DrawableRes

data class AppModel(
    val name: String,
    val url: String,
    val isBookmark: Boolean,
    @DrawableRes val icon: Int
)