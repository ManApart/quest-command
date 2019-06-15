package core.commands

import core.utility.Named

abstract class Command : Named {

    abstract fun getAliases(): Array<String>
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>
    abstract fun execute(keyword: String, args: List<String>)

    override val name = getAliases()[0]
}
