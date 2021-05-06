package magic.spellCommands.water

import traveling.position.Distances
import traveling.position.TargetAim
import traveling.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.target.Target
import status.stat.WATER_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Heal : SpellCommand() {
    override val name = "Heal"

    override fun getDescription(): String {
        return "Heal yourself or others."
    }

    override fun getManual(): String {
        return """
	Cast Heal <amount> for <duration> on *<targets> - Heals damage taken over time."""
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialAmount = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Heal how much?",
            options.associateWith { "cast heal $it for ${initialDuration.toString()} on ${targets.toCommandString()}" })
        val durationResponse = ResponseRequest("Heal for how long?",
            options.associateWith { "cast heal ${initialAmount.toString()} for $it on ${targets.toCommandString()}" })
        val responseHelper = ResponseRequestHelper(mapOf(
                "amount" to ResponseRequestWrapper(initialAmount, amountResponse, useDefaults, 5),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val amount = responseHelper.getIntValue("amount")
            val duration = responseHelper.getIntValue("duration")
            val hitCount = targets.count()
            val totalCost = amount * hitCount
            val levelRequirement = amount * 2 + duration / 2

            executeWithWarns(source, WATER_MAGIC, levelRequirement, totalCost, targets) {
                targets.forEach { target ->
                    val parts = getTargetedPartsOrRootPart(target)
                    val effects = listOf(
                            EffectManager.getEffect("Heal", amount, duration, parts),
                            EffectManager.getEffect("Wet", 0, duration + 1, parts)
                    )

                    val condition = Condition("Healing", Element.WATER, amount, effects)
                    val spell = Spell("Heal", condition, amount, WATER_MAGIC, levelRequirement, range = Distances.SWORD_RANGE)
                    EventManager.postEvent(StartCastSpellEvent(source, target, spell))
                }
            }
        }
    }

}