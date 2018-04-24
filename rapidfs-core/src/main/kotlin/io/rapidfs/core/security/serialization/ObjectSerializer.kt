package io.rapidfs.core.security.serialization

import org.nustaq.serialization.FSTConfiguration
import java.nio.charset.StandardCharsets

object ObjectSerializer {
    const val serializedPrefix: String = "(*[Serialized_]^#)"

    private val configuration: FSTConfiguration by lazy {
        FSTConfiguration.createDefaultConfiguration()
    }

    fun deserialize(array: ByteArray): Any =
            configuration.asObject(array)

    fun serialize(any: Any): ByteArray =
            configuration.asByteArray(any)

}