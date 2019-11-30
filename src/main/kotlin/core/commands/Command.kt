package core.commands

import core.gameState.GameState
import core.gameState.Target
import core.utility.Named

abstract class Command : Named {

    abstract fun getAliases(): Array<String>
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>

    open fun execute(keyword: String, args: List<String>) {
        execute(GameState.player, keyword, args)
    }

    open fun execute(source: Target, keyword: String, args: List<String>) {
        execute(keyword, args)
    }

    override val name = getAliases()[0]
}
