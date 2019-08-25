package combat.approach

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import core.utility.StringFormatter

class Approach : EventListener<ApproachEvent>() {

    override fun execute(event: ApproachEvent) {
        if (GameState.battle != null) {
            if (event.isApproaching) {
                event.source.position.closer(GameState.battle!!.getOponent(event.source)!!.creature.position, event.amount)
            } else {
                event.source.position.further(GameState.battle!!.getOponent(event.source)!!.creature.position, event.amount)
            }
            //TODO - adjust amount by max allowed to move and max distances
            val approachString = StringFormatter.format(event.isApproaching, "closer", "further")
            display("${event.source} moved ${event.amount} $approachString.")
        }
    }

}