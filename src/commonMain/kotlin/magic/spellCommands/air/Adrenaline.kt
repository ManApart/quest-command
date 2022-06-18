package magic.spellCommands.air

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
import status.stat.AGILITY
import status.stat.AIR_MAGIC
import traveling.position.Distances
import traveling.position.ThingAim
import traveling.position.toCommandString

class Adrenaline : SpellCommand() {
    override val name = "Adrenaline"

    override fun getDescription(): String {
        return "Increase how fast you can attack."
    }

    override fun getManual(): String {
        return """
	Cast Adrenaline <amount> for <duration> on *<things> - Increase action point gain. The higher the amount, faster the action point gain. Hindered by encumbrance and enhanced by higher agility."""
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
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
            respond("amount") {
                message("Increase how much?")
                options(options)
                command { "cast adrenaline $it for ${initialDuration.toString()} on ${things.toCommandString()}" }
                value(initialPower)
                useDefault(useDefaults)
                defaultValue(5)
            }
            respond("duration") {
                message("Increase for how long?")
                options(options)
                command { "cast adrenaline ${initialPower.toString()} for $it on ${things.toCommandString()}" }
                value(initialDuration)
                useDefault(useDefaults)
                defaultValue(1)
            }
        }

        if (!clarifier.hasAllValues()) {
            clarifier.requestAResponse()
        } else {
            val power = clarifier.getIntValue("amount")
            val amount = power + ((source.soul.getStatOrNull(AGILITY)?.current ?: 0) * source.thing.getEncumbranceInverted()).toInt()
            val duration = clarifier.getIntValue("duration")
            val hitCount = things.count()
            val perThingCost = power / 10
            val totalCost = perThingCost * hitCount
            val levelRequirement = power / 2

            executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, things) {
                things.forEach { thing ->
                    val parts = getThingedPartsOrRootPart(thing)
                    val effects = listOf(
                        EffectManager.getEffect("Action Point Boost", amount, duration, parts),
                        EffectManager.getEffect("Air Blasted", 0, duration + 1, parts)
                    )

                    val condition = Condition("On Adrenaline", Element.AIR, amount, effects)
                    val spell = Spell("Adrenaline", condition, amount, AIR_MAGIC, levelRequirement, range = Distances.DAGGER_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source.thing, thing, spell))
                }
            }
        }
    }
}