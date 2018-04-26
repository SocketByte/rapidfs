package io.rapidfs.core.command.executables

import io.rapidfs.core.RapidFS
import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand

object UpdateCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        val dbArgument = command.getArgument("-db")
        val remArgument = command.getArgument("-rem")
        val db = dbArgument?.content
        val database = RapidFS.databaseFactory.getDatabase(db!!)

        if (remArgument != null) {
            database?.finishRemoval()
            return
        }

        database?.finish()
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("update")
                .addArgument("-db")
                .addArgument("-rem", false)
                .setExecutable(this)
    }
}