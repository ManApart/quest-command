package system.alias

import core.GameState
import core.events.EventListener
import core.history.display
import core.history.displayYou

class DeleteAlias : EventListener<DeleteAliasEvent>() {
    override fun execute(event: DeleteAliasEvent) {
        if (GameState.aliases.containsKey(event.alias.lowercase())) {
            GameState.aliases.remove(event.alias.lowercase())
            event.source.displayYou("Removed alias " + event.alias)
        } else {
            event.source.displayYou("No alias exists for " + event.alias)
        }
    }
}