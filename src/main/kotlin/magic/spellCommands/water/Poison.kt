package magic.spellCommands.water

import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.events.EventManager
import core.thing.Thing
import magic.Element
import magic.castSpell.StartCastSpellEvent
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

    override fun execute(source: Thing, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialAmount = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Poison how much?",
            options.associateWith { "cast poison $it for ${initialDuration.toString()} on ${things.toCommandString()}" })
        val durationResponse = ResponseRequest("Poison for how long?",
            options.associateWith { "cast poison ${initialAmount.toString()} for $it on ${things.toCommandString()}" })
        val responseHelper = ResponseRequestHelper(mapOf(
                "amount" to ResponseRequestWrapper(initialAmount, amountResponse, useDefaults, 1),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 10)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val amount = responseHelper.getIntValue("amount")
            val duration = responseHelper.getIntValue("duration")
            val hitCount = things.count()
            val totalCost = (amount * hitCount * 2) + (duration / 4)
            val levelRequirement = amount/2

            executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, things) {
                things.forEach { thing ->
                    val parts = getThingedPartsOrRootPart(thing)
                    val effects = listOf(
                            EffectManager.getEffect("Poison", amount, duration, parts),
                            EffectManager.getEffect("Wet", 0, duration + 1, parts)
                    )

                    val condition = Condition("Poisoned", Element.WATER, amount, effects)
                    val spell = Spell("Poison", condition, amount, WATER_MAGIC, levelRequirement, range = Distances.SWORD_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source, thing, spell))
                }
            }
        }
    }

}