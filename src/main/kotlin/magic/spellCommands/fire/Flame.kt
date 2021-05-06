package magic.spellCommands.fire

import traveling.position.Distances
import traveling.position.TargetAim
import traveling.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.target.Target
import status.stat.FIRE_MAGIC
import magic.castSpell.StartCastSpellEvent
import magic.castSpell.getTargetedPartsOrAll
import magic.spellCommands.SpellCommand
import magic.spells.Spell
import status.conditions.Condition
import status.effects.EffectManager
import magic.Element
import core.events.EventManager
import core.events.multiEvent.StartMultiEvent
import kotlin.math.max

class Flame : SpellCommand() {
    override val name = "Flame"

    override fun getDescription(): String {
        return "High damage short range attack that leaves the target burning."
    }

    override fun getManual(): String {
        return """
	Cast Flame <power> on *<targets> - High damage short range attack that leaves the target burning. Burns caster a proportional amount (minus immunity)."""
    }

    override fun getCategory(): List<String> {
        return listOf("Fire")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val initialPower = args.getBaseNumber()

        val powerOptions = listOf("1", "3", "5", "10", "50", "#")
        val powerResponse = ResponseRequest("Cast Flame with what power?",
            powerOptions.associateWith { "cast flame $it on ${targets.toCommandString()}" })

        val responseHelper = ResponseRequestHelper(mapOf(
                "power" to ResponseRequestWrapper(initialPower, powerResponse, useDefaults, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val damageAmount = responseHelper.getIntValue("power")
            val cost = damageAmount / 2
            val totalCost = cost * targets.count()
            val levelRequirement = damageAmount / 2

            executeWithWarns(source, FIRE_MAGIC, levelRequirement, totalCost, targets) {
                val spells = mutableListOf(
                        postSpell(source, TargetAim(source, source.body.getParts()), max(1, damageAmount / 3), 0, levelRequirement)
                )
                targets.forEach { target ->
                    spells.add(postSpell(source, target, damageAmount, cost, levelRequirement))
                }

                EventManager.postEvent(StartMultiEvent(source, spells.first().timeLeft, spells))
            }
        }
    }

    private fun postSpell(source: Target, target: TargetAim, damageAmount: Int, cost: Int, levelRequirement: Int): StartCastSpellEvent {
        val parts = getTargetedPartsOrAll(target, 3)
        val effects = listOf(
                EffectManager.getEffect("Burning", damageAmount, 3, parts),
                EffectManager.getEffect("On Fire", damageAmount, 3, parts)
        )

        val condition = Condition("Fire Blasted", Element.FIRE, cost, effects)
        val spell = Spell("Flame", condition, cost, FIRE_MAGIC, levelRequirement, range = Distances.SPEAR_RANGE)
        return StartCastSpellEvent(source, target, spell)
    }

}