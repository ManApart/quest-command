package combat.slash

import core.events.EventListener

class Slash : EventListener<SlashEvent>() {
    override fun execute(event: SlashEvent) {
       println("You slash ${event.target} with your ${event.sourcePart.equippedName()}.")
    }



}