package combat.battle

import combat.chop.ChopEvent
import combat.crush.CrushEvent
import combat.slash.SlashEvent
import combat.stab.StabEvent
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target
import system.EventManager
import system.gameTick.GameTickEvent

object StartBattle {

    class Chop : EventListener<ChopEvent>() {
        override fun execute(event: ChopEvent) {
            startBattle(event.source, event.target)
        }
    }

    class Crush : EventListener<CrushEvent>() {
        override fun execute(event: CrushEvent) {
            startBattle(event.source, event.target)
        }
    }

    class Stab : EventListener<StabEvent>() {
        override fun execute(event: StabEvent) {
            startBattle(event.source, event.target)
        }
    }

    class Slash : EventListener<SlashEvent>() {
        override fun execute(event: SlashEvent) {
            startBattle(event.source, event.target)
        }
    }

    fun startBattle(aggressor: Creature, victim: Target) {
        if (victim is Creature && GameState.battle == null){
            GameState.battle = Battle(listOf(aggressor, victim))
            EventManager.postEvent(GameTickEvent())
        }
    }


}