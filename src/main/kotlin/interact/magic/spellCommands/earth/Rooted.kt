package interact.magic.spellCommands.earth

import combat.battle.position.TargetAim
import combat.battle.position.toCommandString
import core.commands.*
import core.gameState.Target
import core.gameState.stat.AGILITY
import core.gameState.stat.AIR_MAGIC
import core.gameState.stat.EARTH_MAGIC
import interact.magic.StartCastSpellEvent
import interact.magic.getTargetedPartsOrRootPart
import interact.magic.spellCommands.SpellCommand
import interact.magic.spells.Spell
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager

class Rooted : SpellCommand() {
    override val name = "Rooted"

    override fun getDescription(): String {
        return "Cast Rooted:\n\tEncase the target in earth to increase their defense."
    }

    override fun getManual(): String {
        return "\n\tCast Rooted <amount> for <duration> on *<targets> - Encase the target in earth to increase their defense. Increases defense by amount * percent encumbered. Fully encumbers target."
    }

    override fun getCategory(): List<String> {
        return listOf("Earth")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("for"))
        val initialPower = spellArgs.getBaseNumber()
        val initialDuration = spellArgs.getNumber("for")

        val options = listOf("1", "3", "5", "10", "50", "#")
        val amountResponse = ResponseRequest("Increase defense how much?", options.map { it to "cast rooted $it for ${initialDuration.toString() ?: ""} on ${targets.toCommandString()}" }.toMap())
        val durationResponse = ResponseRequest("Increase for how long?", options.map { it to "cast rooted ${initialPower.toString() ?: ""} for $it on ${targets.toCommandString()}" }.toMap())
        val responseHelper = ResponseRequestHelper(mapOf(
                "power" to ResponseRequestWrapper(initialPower, amountResponse, useDefaults, 5),
                "duration" to ResponseRequestWrapper(initialDuration, durationResponse, useDefaults, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val power = responseHelper.getIntValue("power")
            val duration = responseHelper.getIntValue("duration")
            val hitCount = targets.count()
            val perTargetCost = power / 10
            val totalCost = perTargetCost * hitCount
            val levelRequirement = power / 2

            executeWithWarns(source, AIR_MAGIC, levelRequirement, totalCost, targets) {
                targets.forEach { target ->
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
}