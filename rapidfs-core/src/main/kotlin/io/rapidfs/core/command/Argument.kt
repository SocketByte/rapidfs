package io.rapidfs.core.command

import java.util.ArrayList

class Argument {

    val name: String
    var aliases = listOf<String>()
        private set

    var content: String? = null
    var isRequired: Boolean = false
        private set

    constructor(required: Boolean, name: String) {
        this.isRequired = required
        this.name = name
    }

    constructor(name: String) {
        this.isRequired = false
        this.name = name
    }

    constructor(name: String, vararg aliases: String) {
        this.isRequired = false
        this.name = name
        this.aliases = aliases.toList()
    }

    constructor(required: Boolean, name: String, vararg aliases: String) {
        this.isRequired = required
        this.name = name
        this.aliases = aliases.toList()
    }

}
