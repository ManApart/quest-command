package core.gameState

import core.gameState.stat.Stat
import status.effects.RemoveEffectEvent
import system.EventManager

class Effect(val name: String, val type: EffectType, val statName: String, val amount: Int = 1, var duration: Int = -1) {
    enum class EffectType { DRAIN, REDUCE, HEAL, BOOST }

    var doOnce = true

    fun applyEffect(soul: Soul) {
        val stat = soul.getStatOrNull(statName)
        if (stat != null) {
            applyEffectToStat(stat)
            decreaseDuration(soul)
        } else {
            soul.effects.remove(this)
        }

    }

    private fun applyEffectToStat(stat: Stat) {
        when (type) {
            EffectType.DRAIN -> stat.current -= amount
            EffectType.HEAL -> stat.current += amount
            EffectType.REDUCE -> {
                if (doOnce) {
                    stat.boostedMax -= amount
                    doOnce = false
                }
            }
            EffectType.BOOST -> {
                if (doOnce) {
                    stat.boostedMax += amount
                    doOnce = false
                }
            }
        }

    }

    private fun decreaseDuration(soul: Soul) {
        if (duration > 0) duration--
        if (duration == 0) {
            EventManager.postEvent(RemoveEffectEvent(soul.creature, this))
        }
    }
}