package status.effects

import combat.takeDamage.TakeDamageEvent
import core.gameState.Soul
import core.gameState.body.BodyPart
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager
import kotlin.math.min

class Effect(val base: EffectBase, val amount: Int, val duration: Int, private val bodyPartTargets: List<BodyPart> = listOf()) {
    private var originalValue = 0

    fun apply(soul: Soul, firstApply: Boolean) {
        soul.parent.getTopParent().properties.tags.add(base.name)
        val stat = soul.getStatOrNull(base.statTarget)
        if (stat != null) {
            val appliedAmount = getAppliedAmount(stat)
            when {
                base.statEffect == StatEffect.DRAIN -> {
                    changeStat(soul, stat, -appliedAmount)
                }
                base.statEffect == StatEffect.DEPLETE && firstApply -> {
                    changeStat(soul, stat, -appliedAmount)
                }
                base.statEffect == StatEffect.BOOST && firstApply -> {
                    originalValue = stat.current
                    changeStat(soul, stat, appliedAmount)
                }
                base.statEffect == StatEffect.RECOVER -> {
                    changeStat(soul, stat, appliedAmount)
                }
                base.statEffect == StatEffect.NONE -> {
                }
            }
        }
    }

    private fun getAppliedAmount(stat: Stat): Int {
        return if (base.amountType == AmountType.FLAT_NUMBER) {
            amount
        } else {
            ((amount / 100f) * stat.getBaseMaxAtCurrentLevel()).toInt()
        }
    }

    fun remove(soul: Soul) {
        soul.parent.getTopParent().properties.tags.remove(base.name)
        val stat = soul.getStatOrNull(base.statTarget)
        if (stat != null) {
            if (base.statEffect == StatEffect.BOOST) {
                restoreValue(soul, stat, getAppliedAmount(stat))
            }
        }
    }

    private fun restoreValue(soul: Soul, stat: Stat, amount: Int) {
        if (stat.current >= originalValue) {
            val adjustment = min(amount, stat.current - originalValue)
            changeStat(soul, stat, -adjustment)
        }
    }

    private fun changeStat(soul: Soul, stat: Stat, amount: Int) {
        if (stat.isHealth()) {
            bodyPartTargets.forEach { bodyPart ->
                EventManager.postEvent(TakeDamageEvent(soul.parent, bodyPart, amount, base.damageType, base.name))
            }
        } else {
            EventManager.postEvent(StatChangeEvent(soul.parent, base.name, stat.name, amount))
        }
    }

}