package system.alias

import core.GameState
import core.events.EventListener
import core.history.StringTable
import core.history.displayToMe

class ListAlias : EventListener<ListAliasesEvent>() {
    override suspend fun complete(event: ListAliasesEvent) {
        if (GameState.aliases.isEmpty()) {
            event.source.displayToMe("There are currently no aliases.")
        } else {
            val input = mutableListOf(listOf("Alias", "Command"))
            input.addAll(GameState.aliases.map { listOf(it.key, it.value) })
            val aliasTable = StringTable(input, 2, rightPadding = 2)
            event.source.displayToMe(aliasTable.getString())
        }
    }
}