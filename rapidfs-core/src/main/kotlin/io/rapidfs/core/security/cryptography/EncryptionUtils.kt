package io.rapidfs.core.security.cryptography

import kotlin.experimental.and
import java.security.MessageDigest



fun ByteArray.toHex(): String {
    val stringBuilder = StringBuilder()
    for (i in 0..this.size) {
        stringBuilder.append(Integer.toString((this[i] and 0xff.toByte()) + 0x100, 16)
                .substring(1))
    }
    return stringBuilder.toString()
}

fun String.encrypt(alghoritm: Alghoritm): String {
    return alghoritm.encrypt(this)
}