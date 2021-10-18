package magic.spellCommands.earth

import core.Player
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
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

    override fun execute(source: Player, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Increase defense how much?",
            options.associateWith { "cast rooted $it for ${initialDuration.toString()} on ${things.toCommandString()}" })
        val durationResponse = ResponseRequest("Increase for how long?",
            options.associateWith { "cast rooted ${initialPower.toString()} for $it on ${things.toCommandString()}" })
        val responseHelper = ResponseRequestHelper(source, mapOf(
                "power" to ResponseRequestWrapper(initialPower, amountResponse, useDefaults, 5),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val power = responseHelper.getIntValue("power")
            val duration = responseHelper.getIntValue("duration")
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