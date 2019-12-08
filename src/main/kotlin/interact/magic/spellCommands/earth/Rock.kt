package interact.magic.spellCommands.earth

import combat.battle.position.TargetAim
import combat.battle.position.toCommandString
import core.commands.Args
import core.commands.ResponseRequest
import core.commands.ResponseRequestHelper
import core.commands.ResponseRequestWrapper
import core.gameState.Target
import core.gameState.stat.EARTH_MAGIC
import interact.magic.StartCastSpellEvent
import interact.magic.getTargetedPartsOrRootPart
import interact.magic.spellCommands.SpellCommand
import interact.magic.spells.Spell
import status.effects.Condition
import status.effects.EffectManager
import status.effects.Element
import system.EventManager
import kotlin.math.max
import kotlin.math.min

class Rock : SpellCommand() {
    override val name = "Rock"

    override fun getDescription(): String {
        return "Cast Rock:\n\tHit the target with a rock."
    }

    override fun getManual(): String {
        return "\n\tCast Rock <power> size <size> on <target> - Hit the target with a rock. Size can be 1, 2, or 3 (small, medium or large). Small size can be rapidly fired while larger sizes do more than linearly more damage, can cause stun, and take longer to fire."
    }

    override fun getCategory(): List<String> {
        return listOf("Earth")
    }

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        val spellArgs = Args(args.getBaseGroup(), listOf("size"))
        val initialPower = spellArgs.getBaseNumber()
        val initialSize = spellArgs.getNumber("size")

        val powerOptions = listOf("1", "3", "5", "10", "50", "#")
        val powerResponse = ResponseRequest("Cast Rock with what power?", powerOptions.map { it to "cast rock $it size ${initialSize ?: ""} on ${targets.toCommandString()}" }.toMap())
        val sizeOptions = listOf("1", "2", "3")
        val sizeResponse = ResponseRequest("Cast Rock with what size?", sizeOptions.map { it to "cast rock ${initialPower ?: ""} size $it on ${targets.toCommandString()}" }.toMap())

        val responseHelper = ResponseRequestHelper(mapOf(
                "power" to ResponseRequestWrapper(initialPower, powerResponse, useDefaults, 1),
                "size" to ResponseRequestWrapper(initialSize, sizeResponse, useDefaults, 2)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val power = responseHelper.getIntValue("power")
            val size = min(0, max(3, responseHelper.getIntValue("size")))
            val totalCost = power / 10
            val levelRequirement = power / 2

            executeWithWarns(source, EARTH_MAGIC, levelRequirement, totalCost, targets, maxTargetCount = 1) {
                val target = targets.first()
                val parts = getTargetedPartsOrRootPart(target)
                val amount = getDamageAmount(size, power)
                val castTime = getCastTime(size, power)

                val effects = mutableListOf(
                        EffectManager.getEffect("Chopped", amount, 1, parts),
                        EffectManager.getEffect("Dirty", 0, 2, parts)
                )

                if (size == 3){
                    effects.add(EffectManager.getEffect("Stunned", 1, 1, parts))
                }

                val condition = Condition("Hit by a Rock", Element.EARTH, amount, effects)
                val spell = Spell("Rock", condition, amount, EARTH_MAGIC, levelRequirement, castTime = castTime)
                EventManager.postEvent(StartCastSpellEvent(source, target, spell))
            }
        }
    }

    private fun getDamageAmount(size: Int, power: Int): Int {
        return when (size) {
            1 -> power / 2
            2 -> power
            else -> (power * 2.5).toInt()
        }
    }

    //TODO - build a stat out test to tweak this and make it balanced
    private fun getCastTime(size: Int, power: Int): Int {
        return when (size) {
            1 -> power / 10
            2 -> power / 5
            else -> power / 2
        }
    }


}