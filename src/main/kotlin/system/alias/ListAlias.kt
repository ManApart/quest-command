package system.alias

import core.GameState
import core.events.EventListener
import core.history.StringTable
import core.history.display
import core.history.displayYou

class ListAlias : EventListener<ListAliasesEvent>() {
    override fun execute(event: ListAliasesEvent) {
        if (GameState.aliases.isEmpty()) {
            event.source.displayYou("There are currently no aliases.")
        } else {
            val input = mutableListOf(listOf("Alias", "Command"))
            input.addAll(GameState.aliases.map { listOf(it.key, it.value) })
            val aliasTable = StringTable(input, 2, rightPadding = 2)
            event.source.displayYou(aliasTable.getString())
        }
    }
}