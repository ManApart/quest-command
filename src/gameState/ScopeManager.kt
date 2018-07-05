package gameState

import events.ArriveEvent
import events.EventListener

object ScopeManager {
    private val targets = mutableListOf<Target>()

    init {
        resetTargets()
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun execute(event: ArriveEvent) {
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
        addTargets(GameState.player.inventory.items)
    }

    fun targetExists(name: String) : Boolean{
        return targets.firstOrNull { it.toString().toLowerCase() == name.toLowerCase() } != null
    }

    fun targetExists(name: List<String>) : Boolean{
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return targets.firstOrNull { fullName.contains(it.toString().toLowerCase()) } != null
    }

    fun getTarget(name: String) : Target {
        return targets.first { it.toString().toLowerCase() == name.toLowerCase() }
    }

    fun getTarget(name: List<String>) : Target {
        val fullName = name.joinToString(" ").toLowerCase()
        return targets.first { fullName.contains(it.toString().toLowerCase()) }
    }

    fun removeTarget(target: Target){
        targets.remove(target)
    }

}