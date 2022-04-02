package com.sealed.ui.them

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.sealed.ui.R
import com.sealed.ui.them.color.MHPLightColor

val mhpColor = MHPLightColor()

private val appFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.roboto_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.roboto_regular,
            weight = FontWeight.W400,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.roboto_medium,
            weight = FontWeight.W600,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.roboto_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
    )
)

@Composable
fun Them(content: @Composable () -> Unit) = MaterialTheme(
    colors = mhpColor.generate(),
    content = content,
    typography = Typography(
        defaultFontFamily = appFontFamily
    )
)