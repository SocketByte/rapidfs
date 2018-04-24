package io.rapidfs.core.security.cryptography

import java.security.MessageDigest

enum class Alghoritm(private val alghoritmName: String) {

    @Deprecated("MD2 is currently insecure and should NOT be used")
    MD2("MD2"),
    @Deprecated("MD5 is currently insecure and should NOT be used")
    MD5("MD5"),

    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    fun encrypt(text: String): String {
        val digest = MessageDigest
                .getInstance(this.alghoritmName)
        val hashedBytes = digest.digest(text.toByteArray(
                java.nio.charset.StandardCharsets.UTF_8))

        return hashedBytes.toHex()
    }

}