package io.rapidfs.core.command

object Locker {
    private var lockCommands = false

    fun isLocked(): Boolean {
        return lockCommands
    }

    fun setLocked(lockCommands: Boolean) {
        Locker.lockCommands = lockCommands
        println()
    }

    fun enable() {
        Locker.lockCommands = true
    }

    fun disable() {
        Locker.lockCommands = false
    }
}