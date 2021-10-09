package system.alias

import core.GameState
import core.events.EventListener
import core.history.displayToMe

class DeleteAlias : EventListener<DeleteAliasEvent>() {
    override fun execute(event: DeleteAliasEvent) {
        if (GameState.aliases.containsKey(event.alias.lowercase())) {
            GameState.aliases.remove(event.alias.lowercase())
            event.source.displayToMe("Removed alias " + event.alias)
        } else {
            event.source.displayToMe("No alias exists for " + event.alias)
        }
    }
}