package magic.spellCommands.earth

import core.Player
import core.commands.Args
import core.commands.clarify
import core.events.EventManager
import magic.Element
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getThingedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import status.stat.EARTH_MAGIC
import traveling.position.ThingAim
import traveling.position.toCommandString

class Rooted : SpellCommand() {
    override val name = "Rooted"

    override fun getDescription(): String {
        return "Encase the thing in earth to increase their defense."
    }

    override fun getManual(): String {
        return """
	Cast Rooted <amount> for <duration> on <thing> - Encase the thing in earth to increase their defense. Increases defense by amount * percent encumbered. Fully encumbers thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Earth")
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("1", "5", "10")
            args.size == 1 && args.last().toIntOrNull() != null -> listOf("for")
            args.last() == "for" -> listOf("1", "5", "10")
            args.size == 3 && args.last().toIntOrNull() != null -> listOf("on")
            args.last() == "on" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")
        val options = listOf("1", "3", "5", "10", "50", "#")

        val clarifier = source.clarify {
            respond("power") {
                message("Increase defense how much?")
                options(options)
                command { "cast rooted $it for ${initialDuration.toString()} on ${things.toCommandString()}" }
                value(initialPower)
                defaultValue(5)
            }
            respond("duration") {
                message("Increase for how long?")
                options(options)
                command { "cast rooted ${initialPower.toString()} for $it on ${things.toCommandString()}" }
                value(initialDuration)
                defaultValue(1)
            }
        }

        if (!clarifier.hasAllValues()) {
            clarifier.requestAResponse()
        } else {
            val power = clarifier.getIntValue("power")
            val duration = clarifier.getIntValue("duration")
            val totalCost = power / 10
            val levelRequirement = power / 2

            executeWithWarns(source, EARTH_MAGIC, levelRequirement, totalCost, things, maxThingCount = 1) {
                val thing = things.first()
                val amount = (power * thing.thing.getEncumbrancePhysicalOnly()).toInt()
                val parts = getThingedPartsOrRootPart(thing)
                val effects = listOf(
                    EffectManager.getEffect("Encased Agility", amount, duration, parts),
                    EffectManager.getEffect("Encased Encumbrance", 100, duration, parts),
                    EffectManager.getEffect("Encased Slash Defense", amount, duration, parts),
                    EffectManager.getEffect("Encased Chop Defense", amount, duration, parts),
                    EffectManager.getEffect("Dirty", 0, duration + 1, parts)
                )

                val condition = Condition("Rooted", Element.EARTH, amount, effects)
                val spell = Spell("Rooted", condition, amount, EARTH_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(source.thing, thing, spell))
            }
        }
    }
}