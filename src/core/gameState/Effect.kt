package core.gameState

import core.gameState.stat.Stat
import status.effects.EffectAppliedEvent
import status.effects.RemoveEffectEvent
import status.statChanged.StatBoostEvent
import status.statChanged.StatChangeEvent
import status.statChanged.StatChanged
import system.EventManager

class Effect(val name: String, val type: EffectType, val statName: String, val amount: Int = 1, var duration: Int = -1) {
    enum class EffectType { DRAIN, REDUCE, HEAL, BOOST }

    var doOnce = true

    fun copy() : Effect {
        return Effect(name, type, statName, amount, duration)
    }

    fun applyEffect(soul: Soul, time: Int) {
        val stat = soul.getStatOrNull(statName)
        if (stat != null) {
            val applied = applyEffectToStat(soul, stat, time)
            if (applied) {
                EventManager.postEvent(EffectAppliedEvent(soul.creature, this))
            }
            decreaseDuration(soul)
        } else {
            soul.effects.remove(this)
        }

    }

    private fun applyEffectToStat(soul: Soul, stat: Stat, time: Int) : Boolean {
        var applied = false
        when (type) {
            EffectType.DRAIN ->{
                EventManager.postEvent(StatChangeEvent(soul.creature, name, stat.name, -amount*time))
                applied = true
            }
            EffectType.HEAL -> {
                EventManager.postEvent(StatChangeEvent(soul.creature, name, stat.name, amount*time))
                applied = true
            }
            EffectType.REDUCE -> {
                if (doOnce) {
                    EventManager.postEvent(StatBoostEvent(soul.creature, name, stat.name, -amount))
                    doOnce = false
                    applied = true
                }
            }
            EffectType.BOOST -> {
                if (doOnce) {
                    EventManager.postEvent(StatBoostEvent(soul.creature, name, stat.name, amount))
                    doOnce = false
                    applied = true
                }
            }
        }
        return applied
    }

    private fun decreaseDuration(soul: Soul) {
        if (duration > 0) duration--
        if (duration == 0) {
            EventManager.postEvent(RemoveEffectEvent(soul.creature, this))
        }
    }
}