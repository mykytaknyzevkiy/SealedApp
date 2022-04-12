package com.sealed.ui.them.color

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

class MHPLightColor {
    val primary = Color.White

    val screenBackground = Color.Black

    internal fun generate(): Colors = lightColors(
        primary = primary,
        background = screenBackground
    )

}