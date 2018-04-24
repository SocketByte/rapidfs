package io.rapidfs.core.command

import java.util.Arrays
import java.util.WeakHashMap

class Command(val name: String) {
    private val arguments = WeakHashMap<String, Argument>()
    private var executable: Executable? = null

    fun addArgument(argument: Argument) {
        val name = argument.name
        if (arguments.containsKey(name))
            arguments.remove(name)

        arguments[name] = argument
    }

    fun hasContent(argument: String): Boolean {
        val arg = getArgument(argument)

        return arg?.content.isNullOrEmpty()
    }

    fun getArgument(name: String): Argument? {
        return arguments[name]
    }

    fun getArguments(): Map<String, Argument> {
        return arguments
    }

    fun updateContent(name: String, content: String) {
        val argument = getArgument(name)
        if (argument != null) {
            argument.content = content
            addArgument(argument)
        }
    }

    fun setExecutable(executable: Executable) {
        this.executable = executable
    }

    fun execute() {
        executable ?: throw NullPointerException("executable can not be null")

        executable!!.execute(this)
        Locker.disable()
    }

    fun clearContents() {
        for (argument in arguments.values) {
            argument.content = null
        }
    }

    fun containsArgument(a: String): Boolean {
        if (getArguments().containsKey(a))
            return true
        var valid = false
        for (argument in getArguments().values) {
            val aliases = argument.aliases
            println(a)

            for (alias in aliases) {
                valid = alias.equals(a, true)

                if (valid)
                    break
            }
        }
        return valid
    }
}
