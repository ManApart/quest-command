package status.effects

import combat.takeDamage.TakeDamageEvent
import core.gameState.Soul
import core.gameState.body.BodyPart
import core.gameState.stat.LeveledStat
import core.gameState.stat.StatKind
import status.propValChanged.PropertyStatChangeEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import kotlin.math.min

class Effect(val base: EffectBase, val amount: Int, val duration: Int, private val bodyPartTargets: List<BodyPart> = listOf()) {
    private var originalValue = 0

    override fun toString(): String {
        return "${base.name} ${base.statEffect} $amount (${base.amountType}) ${base.statTarget} (${base.statKind}) for $duration."
    }

    fun apply(soul: Soul, firstApply: Boolean) {
        soul.parent.getTopParent().properties.tags.add(base.name)

        if (base.statKind == StatKind.LEVELED) {
            applyLeveledStat(soul, firstApply)
        } else {
            applyStatValue(soul, firstApply)
        }

    }

    private fun applyLeveledStat(soul: Soul, firstApply: Boolean) {
        val stat = getEffectedStat(soul)
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

    private fun applyStatValue(soul: Soul, firstApply: Boolean) {
        val values = soul.parent.properties.values
        if (base.statTarget != null) {
            if (!values.hasInt(base.statTarget)) {
                values.put(base.statTarget, 0)
            }
            when {
                base.statEffect == StatEffect.DRAIN -> {
                    EventManager.postEvent(PropertyStatChangeEvent(soul.parent, base.name, base.statTarget, -amount))
                }
                base.statEffect == StatEffect.DEPLETE && firstApply -> {
                    EventManager.postEvent(PropertyStatChangeEvent(soul.parent, base.name, base.statTarget, -amount))
                }
                base.statEffect == StatEffect.BOOST && firstApply -> {
                    originalValue = values.getInt(base.statTarget)
                    EventManager.postEvent(PropertyStatChangeEvent(soul.parent, base.name, base.statTarget, amount))
                }
                base.statEffect == StatEffect.RECOVER -> {
                    EventManager.postEvent(PropertyStatChangeEvent(soul.parent, base.name, base.statTarget, amount))
                }
                base.statEffect == StatEffect.NONE -> {
                }
            }
        }
    }

    private fun getEffectedStat(soul: Soul): LeveledStat? {
        return soul.getStatOrNull(base.statTarget)
    }

    private fun getAppliedAmount(leveledStat: LeveledStat): Int {
        return if (base.amountType == AmountType.FLAT_NUMBER) {
            amount
        } else {
            ((amount / 100f) * leveledStat.getBaseMaxAtCurrentLevel()).toInt()
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

    private fun restoreValue(soul: Soul, leveledStat: LeveledStat, amount: Int) {
        if (leveledStat.current >= originalValue) {
            val adjustment = min(amount, leveledStat.current - originalValue)
            changeStat(soul, leveledStat, -adjustment)
        }
    }

    private fun changeStat(soul: Soul, leveledStat: LeveledStat, amount: Int) {
        if (leveledStat.isHealth() && amount < 0) {
            bodyPartTargets.forEach { bodyPart ->
                EventManager.postEvent(TakeDamageEvent(soul.parent, bodyPart, -amount, base.damageType, base.name))
            }
        } else {
            EventManager.postEvent(StatChangeEvent(soul.parent, base.name, leveledStat.name, amount))
        }
    }

}