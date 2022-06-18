package core.commands

import core.Player
import core.thing.Thing
import core.utility.Named

abstract class Command : Named {

    abstract fun getAliases(): List<String>
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>

//    abstract fun suggest(source: Player, keyword: String, args: List<String>): List<String>
    open fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    //Temporarily make both open until we phase this one out
    open fun execute(source: Thing, keyword: String, args: List<String>){}

    open fun execute(source: Player, keyword: String, args: List<String>){
        execute(source.thing, keyword, args)
    }

    fun isAlias(word: String): Boolean {
        return getAliases().contains(word.lowercase())
    }

    override val name = getAliases()[0]
}
