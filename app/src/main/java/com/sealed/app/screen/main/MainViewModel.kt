package com.sealed.app.screen.main

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sealed.app.widget.QuickAppWidget
import com.sealed.repository.AppRepository
import com.sealed.repository.Builder
import com.sealed.repository.model.AppModel
import com.sealed.repository.unit.StateImp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalFoundationApi
fun AppRepository.appListFlow(context: Context) = this.apps.onEach {
    GlanceAppWidgetManager(context)
        .getGlanceIds(QuickAppWidget::class.java)
        .forEach { id -> QuickAppWidget(
            it.map { aM ->
                aM.apply {
                    iconBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, Uri.parse(aM.icon)))
                    } else {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(aM.icon))
                    }
                }
            }
        ).update(context, id) }
}

@ExperimentalFoundationApi
class MainViewModel : ViewModel() {

    private val appRepository = AppRepository()

    val apps = MutableStateFlow<StateImp<List<AppModel>>>(
        StateImp.Loading()
    )

    fun load(context: Context) {
        appRepository.appListFlow(context = context)
            .onEach {
                apps.value = StateImp.Data(it)
            }
            .launchIn(viewModelScope)
    }

    fun vncUrl(context: Context) = appRepository.vncUrl(context)

    fun changeVncUrl(context: Context, url: String) = appRepository.changeVncUrl(context, url)

}