package com.d2digital.loger

import android.util.Log
import com.google.gson.Gson
import java.util.regex.Pattern

object Logger {

    internal enum class LogType {
        ERROR,
        INFO
    }

    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

    fun info(message: List<Any>) {
        log(LogType.INFO, Gson().toJson(message))
    }

    fun info(message: String) {
        log(LogType.INFO, message)
    }

    fun error(message: String? = null, err: Exception? = null) {
        log(LogType.ERROR, message ?: "error", err)
    }

    private fun log(type: LogType, msg: String, err: Exception? = null) {
        val nTag = tag

        when (type) {
            LogType.ERROR -> Log.e(nTag, msg, err)
            LogType.INFO  -> Log.i(nTag, msg)
        }
    }

    private val tag: String
        get() = Throwable().stackTrace
            .first()
            .let(::createStackElementTag)

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        // Tag length limit was removed in API 26.
        return tag
    }
}