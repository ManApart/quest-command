package magic.spellCommands.water

import core.Player
import core.commands.Args
import core.commands.clarify
import core.events.EventManager
import magic.Element
import magic.castSpell.CastSpellEvent
import magic.castSpell.getThingedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.WATER_MAGIC
import traveling.position.Distances
import traveling.position.ThingAim
import traveling.position.toCommandString

class Poison : SpellCommand() {
    override val name = "Poison"

    override fun getDescription(): String {
        return "Poison a thing, doing damage over time."
    }

    override fun getManual(): String {
        return """
	Cast Poison <amount> for <duration> on *<things> - Does damage over time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("1", "5", "10")
            args.size == 1 && args.last().toIntOrNull() != null -> listOf("for")
            args.last() == "for" -> listOf("1", "5", "10")
            args.size == 3 && args.last().toIntOrNull() != null -> listOf("on")
            args.last() == "on" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialAmount = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")
        val options = listOf("1", "3", "5", "10", "50", "#")

        val clarifier = source.clarify {
            respond("amount") {
                message("Poison how much?")
                options(options)
                command { "cast poison $it for ${initialDuration.toString()} on ${things.toCommandString()}"  }
                value(initialAmount)
                defaultValue(1)
            }
            respond("duration") {
                message("Poison for how long?")
                options(options)
                command { "cast poison ${initialAmount.toString()} for $it on ${things.toCommandString()}" }
                value(initialDuration)
                defaultValue(10)
            }
        }

        if (!clarifier.hasAllValues()) {
            clarifier.requestAResponse()
        } else {
            val amount = clarifier.getIntValue("amount")
            val duration = clarifier.getIntValue("duration")
            val hitCount = things.count()
            val totalCost = (amount * hitCount * 2) + (duration / 4)
            val levelRequirement = amount / 2

            executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, things) {
                things.forEach { thing ->
                    val parts = getThingedPartsOrRootPart(thing)
                    val effects = listOf(
                        EffectManager.getEffect("Poison", amount, duration, parts),
                        EffectManager.getEffect("Wet", 0, duration + 1, parts)
                    )

                    val condition = Condition("Poisoned", Element.WATER, amount, effects)
                    val spell = Spell("Poison", condition, amount, WATER_MAGIC, levelRequirement, range = Distances.SWORD_RANGE)
                    EventManager.postEvent(CastSpellEvent(source.thing, thing, spell))
                }
            }
        }
    }

}