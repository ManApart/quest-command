package magic.spellCommands.water

import core.Player
import core.commands.Args
import core.events.EventManager
import magic.Element
import magic.castSpell.CastSpellEvent
import magic.castSpell.getThingedPartsOrAll
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.MagicSkills.WATER_MAGIC
import traveling.position.Distances
import traveling.position.ThingAim

class Jet : SpellCommand() {
    override val name = "Jet"

    override fun getDescription(): String {
        return "Burst of water that does one time damage to one or more things."
    }

    override fun getManual(): String {
        return """
	Cast Jet <damage amount> on *<things> - Burst of water that does one time damage to one or more things."""
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("1", "5", "10")
            args.size == 1 && args.last().toIntOrNull() != null -> listOf("on")
            args.last() == "on" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        //TODO - response request instead of hard coded default
        val damageAmount = args.getNumber() ?: 1
        val hitCount = things.sumOf { getThingedPartsOrAll(it).size }
        val totalCost = damageAmount * hitCount
//        val levelRequirement = damageAmount + (hitCount / 2)
        val levelRequirement = damageAmount/2

        executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, things) {
            things.forEach { thing ->
                val parts = getThingedPartsOrAll(thing)
                val effects = listOf(
                        EffectManager.getEffect("Water Slice", damageAmount, 1, parts),
                        EffectManager.getEffect("Wet", 0, 5, parts)
                )

                val cost = damageAmount * parts.size
                val condition = Condition("Water Blasted", Element.WATER, cost, effects)
                val spell = Spell("Jet", condition, cost, WATER_MAGIC, levelRequirement, range = Distances.SPEAR_RANGE)
                EventManager.postEvent(CastSpellEvent(source.thing, thing, spell))
            }
        }
    }

}