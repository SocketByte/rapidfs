package io.rapidfs.core.command.executables

import io.rapidfs.core.command.Command
import io.rapidfs.core.command.CommandBuilder
import io.rapidfs.core.command.ExecutableCommand
import io.rapidfs.core.command.exceptions.ArgumentRequiredException

object TestCommand : ExecutableCommand() {
    override fun execute(command: Command) {
        println(command.getArgument("-key")?.content ?: throw ArgumentRequiredException("-key"))
    }

    override fun getBuilder(): CommandBuilder {
        return CommandBuilder("test")
                .addArgument("-key")
                .setExecutable(this)
    }
}