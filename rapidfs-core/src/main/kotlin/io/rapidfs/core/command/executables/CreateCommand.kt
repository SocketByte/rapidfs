package io.rapidfs.core.command.executables

import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand

object CreateCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        val dbArgument = command.getArgument("-db")

        val db = dbArgument?.content

        RapidFS.databaseFactory.createDatabase(db!!)
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("create")
                .addArgument("-db")
                .setExecutable(this)
    }
}