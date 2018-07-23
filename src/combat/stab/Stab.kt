package battle.stab

import core.events.EventListener

class Stab : EventListener<StabEvent>() {
    override fun execute(event: StabEvent) {
       println("You stab ${event.target} with your ${event.sourcePart.equippedName()}.")
    }



}