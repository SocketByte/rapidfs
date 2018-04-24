package io.rapidfs.core.security

import com.esotericsoftware.minlog.Log.*
import io.rapidfs.core.RapidFS
import java.util.*

object StackTraceHandler {
    fun handle(e: Throwable) {
        severe("An error occured. Please copy the log and contact RapidFS team")
        severe()
        severe("Exception:  ${e.toString()
                .replace(": " + e.message, "")
                .replace("java.lang.", "")}")
        severe("Message:  ${e.message}")
        severe("Cause:  ${e.cause}")
        severe("Timestamp:  ${Date(System.currentTimeMillis())}")
        severe("Version:  ${RapidFS.mavenModel.version} (${RapidFS.mavenModel.id})")
        severe()
        createStackTrace(*e.stackTrace)
    }

    private fun createStackTrace(vararg elements: StackTraceElement) {
        for (element in elements) {
            severe("at ${element.className} [${element.methodName}()" +
                    ":${if (element.lineNumber < 0) "?" else element.lineNumber.toString()}] " +
                    if (element.isNativeMethod) "(Native)" else "")
        }
    }
}