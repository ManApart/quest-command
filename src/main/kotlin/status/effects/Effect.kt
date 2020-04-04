package status.effects

import combat.takeDamage.TakeDamageEvent
import core.events.EventManager
import core.properties.propValChanged.PropertyStatChangeEvent
import core.utility.Named
import status.Soul
import status.stat.AmountType
import status.stat.LeveledStat
import status.stat.StatEffect
import status.stat.StatKind
import status.statChanged.StatChangeEvent
import traveling.location.location.Location
import kotlin.math.min

class Effect(val base: EffectBase, val amount: Int, val duration: Int, val bodyPartTargets: List<Location> = listOf()) : Named {
    var originalValue = 0; private set
    override val name = base.name

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
        if (base.statEffect == StatEffect.BOOST || base.statEffect == StatEffect.DEPLETE) {
            if (base.statKind == StatKind.LEVELED) {
                val stat = soul.getStatOrNull(base.statTarget)
                if (stat != null) {
                    restoreValue(soul, stat, getAppliedAmount(stat))
                }
            } else {
                restorePropertyVal(soul)
            }
        }
    }

    private fun restoreValue(soul: Soul, leveledStat: LeveledStat, amount: Int) {
        if (leveledStat.current >= originalValue) {
            val adjustment = min(amount, leveledStat.current - originalValue)
            changeStat(soul, leveledStat, -adjustment, "Removing ")
        }
    }

    private fun restorePropertyVal(soul: Soul) {
        if (base.statTarget != null) {
            if (!soul.parent.properties.values.hasInt(base.statTarget)) {
                soul.parent.properties.values.put(base.statTarget, 0)
            }
            if (base.statEffect == StatEffect.DEPLETE) {
                EventManager.postEvent(PropertyStatChangeEvent(soul.parent, "Removing " + base.name, base.statTarget, amount))
            } else if (base.statEffect == StatEffect.BOOST) {
                EventManager.postEvent(PropertyStatChangeEvent(soul.parent, "Removing " + base.name, base.statTarget, -amount))
            }
        }
    }

    private fun changeStat(soul: Soul, leveledStat: LeveledStat, amount: Int, sourceOfChangePrefix: String = "") {
        if (leveledStat.isHealth() && amount < 0) {
            bodyPartTargets.forEach { bodyPart ->
                EventManager.postEvent(TakeDamageEvent(soul.parent, bodyPart, -amount, base.damageType, sourceOfChangePrefix + base.name))
            }
        } else {
            EventManager.postEvent(StatChangeEvent(soul.parent, sourceOfChangePrefix + base.name, leveledStat.name, amount))
        }
    }

}