package gameState

import events.ArriveEvent
import events.EventListener

object ScopeManager {
    private val targets = mutableListOf<Target>()

    init {
        resetTargets()
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun handle(event: ArriveEvent) {
            ScopeManager.resetTargets()
        }
    }

    fun addTarget(target: Target) {
        targets.add(target)
    }

    fun addTargets(targets: List<Target>) {
        this.targets.addAll(targets)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    private fun resetTargets() {
        targets.clear()
        addTarget(GameState.player)
        addTargets(GameState.player.items)
    }

}