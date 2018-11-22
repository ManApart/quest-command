package explore

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Target
import core.gameState.targetsToString
import core.history.display
import interact.ScopeManager
import travel.climb.ClimbJourney

class RestrictLocation : EventListener<RestrictLocationEvent>() {


    override fun execute(event: RestrictLocationEvent) {
        event.source.getLink(event.destination)?.restricted = event.makeRestricted
        event.destination.getLink(event.source)?.restricted = event.makeRestricted

        if (!event.silent) {
            if (event.makeRestricted) {
                display("You can no longer access ${event.destination.locationName} from ${event.source.locationName}")
            } else {
                display("You can now access ${event.destination.locationName} from ${event.source.locationName}")
            }
        }
    }


}