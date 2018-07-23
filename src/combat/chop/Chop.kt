package battle.chop

import core.events.EventListener

class Chop : EventListener<ChopEvent>() {
    override fun execute(event: ChopEvent) {
       println("You chop ${event.target} with your ${event.sourcePart.equippedName()}.")
    }



}