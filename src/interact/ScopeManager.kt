package interact

import travel.ArriveEvent
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Target
import system.ActivatorManager
import system.ItemManager

object ScopeManager {
    private val targets = mutableListOf<Target>()

    init {
        resetTargets()
    }

    class ArrivalHandler() : EventListener<ArriveEvent>() {
        override fun execute(event: ArriveEvent) {
            resetTargets()
            addTargets(ItemManager.getItems(event.destination.items))
            addTargets(ActivatorManager.getActivators(event.destination.activators))
        }
    }

    fun addTarget(target: Target) {
        if (!targets.contains(target)){
            targets.add(target)
        }
    }

    fun addTargets(targets: List<Target>) {
        ScopeManager.targets.addAll(targets)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    private fun resetTargets() {
        targets.clear()
        addTarget(GameState.player)
    }

    fun targetExists(name: String) : Boolean{
        return targets.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun targetExists(name: List<String>) : Boolean{
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return targets.firstOrNull { fullName.contains(it.toString().toLowerCase()) } != null
    }

    fun getTarget(name: String) : Target {
        return targets.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getTarget(name: List<String>) : Target {
        val fullName = name.joinToString(" ").toLowerCase()
        return targets.first { fullName.contains(it.toString().toLowerCase()) }
    }

    fun removeTarget(target: Target){
        targets.remove(target)
    }

}