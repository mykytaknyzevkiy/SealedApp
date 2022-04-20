package com.sealed.repository

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.sealed.repository.model.AppModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppRepository {

    private val db by lazy {
        Firebase.firestore
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    fun appsListCache(context: Context) = context
        .getSharedPreferences("sealed", Context.MODE_PRIVATE)
        .getString("appsListCache", "[]")!!

    @SuppressLint("CommitPrefEdits")
    fun updateListCache(context: Context, data: String) = context
        .getSharedPreferences("sealed", Context.MODE_PRIVATE)
        .edit()
        .putString("appsListCache", data)
        .commit()

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
        val documents = db.collection("apps")
            .get()
            .await()
            .documents

        documents.map {
            AppModel(
                name = it["name"] as String,
                url = it["url"] as String,
                isBookmark = it["bookmark"] as Boolean,
                icon = it["icon"] as String
            )
        }.also {
            emit(it)
        }
    }
        .map {
        it.map { appModel ->
            val localStoreFile = File(Builder.appContext.cacheDir, "app_icon_${appModel.icon}")

            if (!localStoreFile.exists())
                storage
                    .getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/sealedapp-dafbc.appspot.com/o/apps_icon/${appModel.icon}")
                    .getFile(localStoreFile)
                    .await()

            appModel.copy(icon = localStoreFile.toUri().toString())
        }
    }
}

private suspend fun Task<QuerySnapshot>.await(): QuerySnapshot = suspendCoroutine { cont ->
    addOnSuccessListener {
        cont.resume(it)
    }
}

@JvmName("awaitUri")
private suspend fun Task<Uri>.await(): Uri = suspendCoroutine { cont ->
    addOnSuccessListener {
        cont.resume(it)
    }
}

private suspend fun FileDownloadTask.await(): Boolean = suspendCoroutine { cont ->
    addOnSuccessListener {
        cont.resume(true)
    }
}