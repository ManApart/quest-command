package magic.spellCommands.earth

import traveling.position.TargetAim
import traveling.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.target.Target
import status.stat.EARTH_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrRootPart
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager

class Rooted : SpellCommand() {
    override val name = "Rooted"

    override fun getDescription(): String {
        return "Encase the target in earth to increase their defense."
    }

    override fun getManual(): String {
        return """
	Cast Rooted <amount> for <duration> on <target> - Encase the target in earth to increase their defense. Increases defense by amount * percent encumbered. Fully encumbers target."""
    }

    override fun getCategory(): List<String> {
        return listOf("Earth")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Increase defense how much?",
            options.associateWith { "cast rooted $it for ${initialDuration.toString()} on ${targets.toCommandString()}" })
        val durationResponse = ResponseRequest("Increase for how long?",
            options.associateWith { "cast rooted ${initialPower.toString()} for $it on ${targets.toCommandString()}" })
        val responseHelper = ResponseRequestHelper(mapOf(
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

            executeWithWarns(source, EARTH_MAGIC, levelRequirement, totalCost, targets, maxTargetCount = 1) {
                val target = targets.first()
                val amount = (power * target.target.getEncumbrancePhysicalOnly()).toInt()
                val parts = getTargetedPartsOrRootPart(target)
                val effects = listOf(
                        EffectManager.getEffect("Encased Agility", amount, duration, parts),
                        EffectManager.getEffect("Encased Encumbrance", 100, duration, parts),
                        EffectManager.getEffect("Encased Slash Defense", amount, duration, parts),
                        EffectManager.getEffect("Encased Chop Defense", amount, duration, parts),
                        EffectManager.getEffect("Dirty", 0, duration + 1, parts)
                )

                val condition = Condition("Rooted", Element.EARTH, amount, effects)
                val spell = Spell("Rooted", condition, amount, EARTH_MAGIC, levelRequirement)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }
}