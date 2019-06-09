package core.gameState

import core.gameState.stat.Stat
import core.utility.Named
import status.effects.EffectAppliedEvent
import status.effects.RemoveEffectEvent
import status.statChanged.StatBoostEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class Effect(override val name: String, private val type: EffectType, private val statName: String, val amount: Int = 1, private var duration: Int = -1) : Named{
    enum class EffectType { DRAIN, REDUCE, HEAL, BOOST }

    private var doOnce = true

    fun copy(): Effect {
        return Effect(name, type, statName, amount, duration)
    }

    fun applyEffect(soul: Soul, time: Int) {
        soul.parent.getTopParent().properties.tags.add(name)
        val stat = soul.getStatOrNull(statName)
        if (stat != null) {
            val applied = applyEffectToStat(soul, stat, time)
            if (applied) {
                EventManager.postEvent(EffectAppliedEvent(soul.parent, this))
            }
            decreaseDuration(soul)
        } else {
            soul.parent.getTopParent().properties.tags.remove(name)
            soul.effects.remove(this)
        }

    }

    private fun applyEffectToStat(soul: Soul, stat: Stat, time: Int): Boolean {
        var applied = false
        when (type) {
            EffectType.DRAIN -> {
                EventManager.postEvent(StatChangeEvent(soul.parent, name, stat.name, -amount * time))
                applied = true
            }
            EffectType.HEAL -> {
                EventManager.postEvent(StatChangeEvent(soul.parent, name, stat.name, amount * time))
                applied = true
            }
            EffectType.REDUCE -> {
                if (doOnce) {
                    EventManager.postEvent(StatBoostEvent(soul.parent, name, stat.name, -amount))
                    doOnce = false
                    applied = true
                }
            }
            EffectType.BOOST -> {
                if (doOnce) {
                    EventManager.postEvent(StatBoostEvent(soul.parent, name, stat.name, amount))
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
            EventManager.postEvent(RemoveEffectEvent(soul.parent, this))
        }
    }
}