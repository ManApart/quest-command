package core.commands

import core.Player
import core.target.Target
import core.utility.Named

abstract class Command : Named {

    abstract fun getAliases(): List<String>
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>
    abstract fun execute(source: Target, keyword: String, args: List<String>)

    open fun execute(source: Player, keyword: String, args: List<String>){
        execute(source.target, keyword, args)
    }



    override val name = getAliases()[0]
}
