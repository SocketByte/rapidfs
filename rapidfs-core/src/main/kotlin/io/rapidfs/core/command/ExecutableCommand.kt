package io.rapidfs.core.command

abstract class ExecutableCommand : Executable {

    abstract fun getBuilder(): CommandBuilder

}