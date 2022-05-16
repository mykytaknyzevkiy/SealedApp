package com.sealed.app.unit

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sealed.app.activity.VncStreamActivity
import com.sealed.repository.AppRepository
import com.sealed.repository.model.AppModel

object VncUnit {

    fun createIntent(context: Context, appModel: AppModel): Intent? {
        val url = AppRepository().vncUrl(context)

        return when {
            appModel.url.startsWith("rdp://") -> Intent(Intent.ACTION_VIEW).apply {
                setClassName("com.iiordanov.freebVNC", "com.iiordanov.bVNC.RemoteCanvasActivity")
                data = Uri.parse("rdp://$url:5901/?ConnectionName=title&RdpUsername=root&RdpPassword=gdc45^2wEdDghT67")
            }
            appModel.url.isNotEmpty() -> Intent(
                context,
                VncStreamActivity::class.java
            ).also {
                context.startActivity(it)
            }
            else -> null
        }
    }

}