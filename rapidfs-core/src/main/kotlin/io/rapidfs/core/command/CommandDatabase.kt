package io.rapidfs.core.command

import io.rapidfs.core.command.exceptions.CommandNotFoundException
import java.util.WeakHashMap

object CommandDatabase {
    private val commandMap = WeakHashMap<String, Command>()

    fun addCommand(command: ExecutableCommand) {
        command.getBuilder().finalize()
    }

    fun addCommand(command: Command) {
        commandMap[command.name] = command
    }

    fun getCommand(name: String): Command {
        if (!commandMap.containsKey(name) )
            throw CommandNotFoundException()

        return commandMap[name]!!
    }
}
