package interact.scope

import core.gameState.*
import core.gameState.Target
import core.utility.NameSearchableList

object ScopeManager {
    private val targets = NameSearchableList<Target>()

    init {
        resetTargets()
    }

    fun addTarget(target: Target, proxies: List<String> = listOf()) {
        if (!targets.contains(target)) {
            targets.add(target)
        }
        if (proxies.isNotEmpty()) {
            targets.addProxy(target, proxies)
        }
    }

    fun addTargets(targets: List<Target>) {
        ScopeManager.targets.addAll(targets)
    }

    fun removeTarget(target: Target) {
        targets.remove(target)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    fun resetTargets() {
        targets.clear()
        addTarget(GameState.player, listOf("me", "self"))
    }

    fun targetExists(name: String): Boolean {
        return targets.exists(name)
    }

    fun targetExists(name: List<String>): Boolean {
        return targets.exists(name)
    }

    fun targetExists(target: Target): Boolean {
        return targets.exists(target)
    }

    fun creatureExists(name: List<String>): Boolean {
        return targets.exists(name) && getTarget(name).getCreature() != null
    }

    fun activatorExists(name: List<String>): Boolean {
        return targets.exists(name) && getTarget(name) is Activator
    }

    fun getTarget(name: String): Target {
        return targets.get(name)
    }

    fun getTarget(name: List<String>): Target {
        return targets.get(name)
    }

    fun getCreature(name: List<String>): Creature {
        return targets.get(name).getCreature()!!
    }

    fun getActivator(name: List<String>): Activator {
        return targets.get(name) as Activator
    }

    fun findTargetByTag(tag: String): Target? {
        return targets.firstOrNull { it.properties.tags.has(tag) }
    }

    fun findActivatorByTag(tag: String): Activator? {
        return targets.firstOrNull { it is Activator && it.properties.tags.has(tag) } as Activator?
    }

    fun findActivatorByTags(tag: Tags): Activator? {
        return targets.firstOrNull { it is Activator && it.properties.tags.hasAll(tag) } as Activator?
    }

    fun findActivatorByProperties(properties: Properties): Activator? {
        return targets.firstOrNull { it is Activator && it.properties.hasAll(properties) } as Activator?
    }

    fun getAllSouls(): List<Soul> {
        val souls = mutableListOf<Soul>()
        souls.add(GameState.player.creature.soul)
        souls.addAll(GameState.player.creature.inventory.getAllItems().map { it.soul })

        getTargets().forEach { target ->
            if (target is Activator) {
                souls.add(target.creature.soul)
            } else if (target is Creature) {
                souls.add(target.soul)
                souls.addAll(target.inventory.getAllItems().map { item -> item.soul })
            }
        }
        return souls.toList()
    }

    fun getAllInventories(): List<Inventory> {
        val inventories = mutableListOf<Inventory>()
        inventories.add(GameState.player.creature.inventory)

        getTargets().forEach {
            if (it is Activator) {
                inventories.add(it.creature.inventory)
            } else if (it is Creature) {
                inventories.add(it.inventory)
            }
        }
        return inventories.toList()
    }

}