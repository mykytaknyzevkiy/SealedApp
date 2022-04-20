package com.sealed.app.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.*
import com.sealed.app.screen.main.appListFlow
import com.sealed.repository.AppRepository
import com.sealed.repository.model.AppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
class QuickAppWidget(private val appsList: List<AppModel>) : GlanceAppWidget() {

    @SuppressLint("FlowOperatorInvokedInComposition")
    @Composable
    override fun Content() {
        val nList = appsList.iterator()

        Column(modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.5f))
        ) {
            while (nList.hasNext()) {
                var num = 0
                Row {
                    while (nList.hasNext() && num < 3) {
                        AppViewHolder(appModel = nList.next())
                        num++
                    }
                }
            }
        }

        if (appsList.isEmpty()) {
            val context = LocalContext.current

            GlobalScope.launch(Dispatchers.IO) {
                AppRepository().appListFlow(context).launchIn(this)
            }
        }
    }

}

@ExperimentalFoundationApi
class QuickAppReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuickAppWidget(emptyList())
}

@Composable
private fun AppViewHolder(appModel: AppModel) {
    Image(
        modifier = GlanceModifier.padding(8.dp).size(65.dp).clickable(
            actionStartActivity(Intent(Intent.ACTION_VIEW).apply {
                val url = AppRepository()
                    .vncUrl(LocalContext.current)
                setClassName("com.iiordanov.freebVNC", "com.iiordanov.bVNC.RemoteCanvasActivity")
                data = Uri.parse("rdp://$url:5901/?ConnectionName=title&RdpUsername=root&RdpPassword=gdc45^2wEdDghT67")
            })
        ),
        provider = BitmapImageProvider(appModel.iconBitmap ?: return),
        contentDescription = null
    )
}