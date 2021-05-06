package magic.spellCommands.water

import traveling.position.Distances
import traveling.position.TargetAim
import core.commands.Args
import core.target.Target
import status.stat.WATER_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrAll
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Jet : SpellCommand() {
    override val name = "Jet"

    override fun getDescription(): String {
        return "Burst of water that does one time damage to one or more targets."
    }

    override fun getManual(): String {
        return """
	Cast Jet <damage amount> on *<targets> - Burst of water that does one time damage to one or more targets."""
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        //TODO - response request instead of hard coded default
        val damageAmount = args.getNumber() ?: 1
        val hitCount = targets.sumOf { getTargetedPartsOrAll(it).size }
        val totalCost = damageAmount * hitCount
//        val levelRequirement = damageAmount + (hitCount / 2)
        val levelRequirement = damageAmount/2

        executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, targets) {
            targets.forEach { target ->
                val parts = getTargetedPartsOrAll(target)
                val effects = listOf(
                        EffectManager.getEffect("Water Slice", damageAmount, 1, parts),
                        EffectManager.getEffect("Wet", 0, 5, parts)
                )

                val cost = damageAmount * parts.size
                val condition = Condition("Water Blasted", Element.WATER, cost, effects)
                val spell = Spell("Jet", condition, cost, WATER_MAGIC, levelRequirement, range = Distances.SPEAR_RANGE)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }

}