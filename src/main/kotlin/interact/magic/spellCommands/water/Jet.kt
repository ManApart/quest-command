package interact.magic.spellCommands.water

import combat.battle.Distances
import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.Target
import core.gameState.stat.WATER_MAGIC
import interact.magic.StartCastSpellEvent
import interact.magic.getTargetedPartsOrAll
import interact.magic.spellCommands.SpellCommand
import interact.magic.spells.Spell
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Jet : SpellCommand() {
    override val name = "Jet"

    override fun getDescription(): String {
        return "Cast Jet:\n\tBurst of water that does one time damage to one or more targets."
    }

    override fun getManual(): String {
        return "\n\tCast Jet <damage amount> on *<targets> - Burst of water that does one time damage to one or more targets."
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        //TODO - response request instead of hard coded default
        val damageAmount = args.getNumber() ?: 1
        val hitCount = targets.sumBy { getTargetedPartsOrAll(it).size }
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