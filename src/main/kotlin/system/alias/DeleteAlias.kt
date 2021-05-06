package system.alias

import core.GameState
import core.events.EventListener
import core.history.display

class DeleteAlias : EventListener<DeleteAliasEvent>() {
    override fun execute(event: DeleteAliasEvent) {
        if (GameState.aliases.containsKey(event.alias.lowercase())) {
            GameState.aliases.remove(event.alias.lowercase())
            display("Removed alias " + event.alias)
        } else {
            display("No alias exists for " + event.alias)
        }
    }
}