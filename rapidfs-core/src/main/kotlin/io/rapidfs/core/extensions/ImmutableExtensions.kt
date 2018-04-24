package io.rapidfs.core.extensions

class ImmutableList<out T>(private val inner:List<T>) : List<T> by inner
class ImmutableMap<K, out V>(private val inner: Map<K, V>) : Map<K, V> by inner

fun <K, V> Map<K, V>.toImmutableMap() = this as? ImmutableMap<K, V> ?: ImmutableMap(this)
fun <T> List<T>.toImmutableList() = this as? ImmutableList<T> ?: ImmutableList(this)