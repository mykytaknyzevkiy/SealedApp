package com.sealed.app.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.AndroidResourceImageProvider
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import com.sealed.repository.R
import com.sealed.repository.model.AppModel

@ExperimentalFoundationApi
class QuickAppWidget : GlanceAppWidget() {

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    override fun Content() {
        val apps = arrayListOf(
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
        ).filter { it.isBookmark }

        LazyVerticalGrid(
            modifier = GlanceModifier.fillMaxSize().background(Color.LightGray),
            gridCells = GridCells.Fixed(3)
        ) {
            items(apps) {
                AppViewHolder(appModel = it)
            }
        }
    }
}

@ExperimentalFoundationApi
class QuickAppReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuickAppWidget()
}

@Composable
private fun AppViewHolder(appModel: AppModel) {
    Image(
        modifier = GlanceModifier.padding(8.dp).size(65.dp).clickable(
            actionStartActivity(Intent(Intent.ACTION_VIEW).apply {
                setClassName("com.iiordanov.freebVNC", "com.iiordanov.bVNC.RemoteCanvasActivity")
                data = Uri.parse("rdp://192.168.48.162:5901/?ConnectionName=title&RdpUsername=root&RdpPassword=gdc45^2wEdDghT67")
            })
        ),
        provider = AndroidResourceImageProvider(appModel.icon),
        contentDescription = null
    )
}