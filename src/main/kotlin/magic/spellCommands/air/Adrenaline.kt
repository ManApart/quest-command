package magic.spellCommands.air

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

    override fun execute(source: Thing, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Increase how much?",
            options.associateWith { "cast adrenaline $it for ${initialDuration.toString()} on ${things.toCommandString()}" })
        val durationResponse = ResponseRequest("Increase for how long?",
            options.associateWith { "cast adrenaline ${initialPower.toString()} for $it on ${things.toCommandString()}" })
        val responseHelper = ResponseRequestHelper(mapOf(
                "amount" to ResponseRequestWrapper(initialPower, amountResponse, useDefaults, 5),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val power = responseHelper.getIntValue("amount")
            val amount = power + ((source.soul.getStatOrNull(AGILITY)?.current ?: 0) * source.getEncumbranceInverted()).toInt()
            val duration = responseHelper.getIntValue("duration")
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
                    EventManager.postEvent(StartCastSpellEvent(source, thing, spell))
                }
            }
        }
    }
}