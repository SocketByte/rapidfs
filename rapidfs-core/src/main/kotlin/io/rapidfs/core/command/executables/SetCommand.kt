package io.rapidfs.core.command.executables

import com.esotericsoftware.minlog.Log.debug
import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand

object SetCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        val dbArgument = command.getArgument("-db")
        val keyArgument = command.getArgument("-k")
        val valueArgument = command.getArgument("-v")

        val db = dbArgument?.content
        val key = keyArgument?.content
        val value = valueArgument?.content

        val database = RapidFS.databaseFactory.getDatabase(db!!)

        database.set(key!!, value)
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("set")
                .addArgument("-db")
                .addArgument("-k")
                .addArgument("-v")
                .setExecutable(this)
    }
}