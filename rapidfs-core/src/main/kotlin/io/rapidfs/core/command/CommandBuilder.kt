package io.rapidfs.core.command

class CommandBuilder internal constructor(private val name: String) {
    private var command: Command? = null

    init {
        this.command = Command(name)
    }

    fun addArgument(name: String): CommandBuilder {
        command!!.addArgument(Argument(name))
        return this
    }

    fun addArgument(name: String, vararg aliases: String): CommandBuilder {
        command!!.addArgument(Argument(name, *aliases))
        return this
    }

    fun addArgument(name: String, required: Boolean): CommandBuilder {
        command!!.addArgument(Argument(required, name))
        return this
    }

    fun addArgument(name: String, required: Boolean, vararg aliases: String): CommandBuilder {
        command!!.addArgument(Argument(required, name, *aliases))
        return this
    }

    fun setExecutable(executable: Executable): CommandBuilder {
        command!!.setExecutable(executable)
        return this
    }

    fun finalize() {
        command?.let {
            CommandDatabase.addCommand(it)
        }
        this.command = null
    }


}
