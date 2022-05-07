package com.sealed.app.ui.view_holder

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.sealed.app.activity.VncStreamActivity
import com.sealed.repository.model.AppModel

@Composable
fun AppViewHolder(
    modifier: Modifier = Modifier,
    appModel: AppModel,
    isNameAvailable: Boolean = true
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.clickable {
            Intent(
                context,
                VncStreamActivity::class.java
            ).also {
                context.startActivity(it)
            }

            /*Intent(
                context,
                WebVncStreamActivity::class.java
            ).also {
                context.startActivity(it)
            }*/
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier.size(65.dp),
            painter = rememberImagePainter(appModel.icon),
            contentDescription = null
        )

        if (isNameAvailable) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = appModel.name, maxLines = 1)
        }
    }
}