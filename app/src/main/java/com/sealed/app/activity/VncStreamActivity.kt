package com.sealed.app.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sealed.app.R
import com.sealed.stream.NativeBindings
import com.sealed.stream.StarStreamView
import com.sealed.stream.StreamView
import com.sealed.stream.StunServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

class VncStreamActivity : AppCompatActivity(), NativeBindings.Listener {

    private val useInsecureTLS = true

    private val bindings = NativeBindings()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_vnc_stream)
        bindings.setListener(this)
    }

    override fun onStreamDisconnected() {
        finish()
    }

    override fun onStart() {
        super.onStart()
        StarStreamView(this).start { sessionURL: String, stunServers: ArrayList<StunServer> ->
            runStream(sessionURL, stunServers)
        }
    }

    private fun runStream(sessionURL: String, stunServers: ArrayList<StunServer>) = GlobalScope.launch(Dispatchers.Main) {
        val streamView = StreamView(this@VncStreamActivity.applicationContext, bindings, sessionURL, stunServers, useInsecureTLS)
        val vg = findViewById<ViewGroup>(android.R.id.content)
        val params = vg.layoutParams
        addContentView(streamView, params)
    }
}