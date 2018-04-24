package io.rapidfs.core.command.executables

import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand

object RemoveCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        val dbArgument = command.getArgument("-db")
        val keyArgument = command.getArgument("-k")

        val db = dbArgument?.content
        val key = keyArgument?.content

        val database = RapidFS.databaseFactory.getDatabase(db!!)

        database.remove(key!!)
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("remove")
                .addArgument("-db")
                .addArgument("-k")
                .setExecutable(this)
    }
}