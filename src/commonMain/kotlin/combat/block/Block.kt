package combat.block

import combat.DamageType
import core.body.BodyPart
import core.events.EventListener
import core.properties.DEFENSE
import core.thing.Thing
import explore.listen.addSoundEffect
import traveling.location.location.Location

class Block : EventListener<BlockEvent>() {

    override suspend fun complete(event: BlockEvent) {
        val blockHelper = event.creature.body.blockHelper
        blockHelper.shield = getShield(event.partThatWillShield)
        blockHelper.blockBodyPart = event.partThatWillShield
        blockHelper.blockedBodyParts.addAll(event.partsThatWillBeShielded)
        event.creature.addSoundEffect("Block", "the tightening of straps and sinew", 1)
    }

    private fun getShield(partThatWillShield: BodyPart): Thing? {
        val equippedItems = partThatWillShield.getEquipped()
        return equippedItems.firstOrNull { it.properties.tags.has("shield") }
                ?: equippedItems.maxByOrNull { getTotalDefense(it) }
    }

    private fun getTotalDefense(apparel: Thing): Int {
        val values = apparel.properties.values
        return values.getInt(DEFENSE) + DamageType.values().sumOf { values.getInt(it.defense) }
    }

}
