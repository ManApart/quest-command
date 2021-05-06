package magic.spellCommands.air

import traveling.position.Distances
import traveling.position.TargetAim
import traveling.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.target.Target
import status.stat.AGILITY
import status.stat.AIR_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Adrenaline : SpellCommand() {
    override val name = "Adrenaline"

    override fun getDescription(): String {
        return "Increase how fast you can attack."
    }

    override fun getManual(): String {
        return """
	Cast Adrenaline <amount> for <duration> on *<targets> - Increase action point gain. The higher the amount, faster the action point gain. Hindered by encumbrance and enhanced by higher agility."""
    }

    override fun getCategory(): List<String> {
        return listOf("Air")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Increase how much?",
            options.associateWith { "cast adrenaline $it for ${initialDuration.toString()} on ${targets.toCommandString()}" })
        val durationResponse = ResponseRequest("Increase for how long?",
            options.associateWith { "cast adrenaline ${initialPower.toString()} for $it on ${targets.toCommandString()}" })
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
            val hitCount = targets.count()
            val perTargetCost = power / 10
            val totalCost = perTargetCost * hitCount
            val levelRequirement = power / 2

            executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, targets) {
                targets.forEach { target ->
                    val parts = getTargetedPartsOrRootPart(target)
                    val effects = listOf(
                            EffectManager.getEffect("Action Point Boost", amount, duration, parts),
                            EffectManager.getEffect("Air Blasted", 0, duration + 1, parts)
                    )

                    val condition = Condition("On Adrenaline", Element.AIR, amount, effects)
                    val spell = Spell("Adrenaline", condition, amount, AIR_MAGIC, levelRequirement, range = Distances.DAGGER_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source, target, spell))
                }
            }
        }
    }
}