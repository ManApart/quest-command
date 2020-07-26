package system.alias

import core.GameState
import core.events.EventListener
import core.history.display

class DeleteAlias : EventListener<DeleteAliasEvent>() {
    override fun execute(event: DeleteAliasEvent) {
        if (GameState.aliases.containsKey(event.alias.toLowerCase())) {
            GameState.aliases.remove(event.alias.toLowerCase())
            display("Removed alias " + event.alias)
        } else {
            display("No alias exists for " + event.alias)
        }
    }
}