package combat.block

import combat.HandHelper
import core.events.EventListener
import core.gameState.GameState
import core.gameState.body.BodyPart

class Block : EventListener<BlockEvent>() {

    override fun execute(event: BlockEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.blockBodyPart = event.partThatWillShield
                combatant.blockedBodyParts.addAll(getBlockedParts(event))
//                display("${event.source} Blockd to the ${event.direction}.")
            }
        }
    }

    private fun getBlockedParts(event: BlockEvent): List<BodyPart> {
        //TODO - get shield, item with most defence, or just bodypart
        val shield = event.partThatWillShield.getEquippedItems().first()
        val shieldSize = shield.properties.values.getInt("radius")
        val partLocation = event.source.body.layout.findLocation(event.partThatWillBeShielded.name)
        val locations = listOf(partLocation) + partLocation.getNeighbors().filter { partLocation.getConnection(it)?.vector?.getDistance() ?: 0 <= shieldSize }

        return locations.mapNotNull { it.getLocation().bodyPart }
    }

}