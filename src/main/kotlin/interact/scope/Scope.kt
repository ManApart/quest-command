package interact.scope

import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class Scope(val locationNode: LocationNode) {
    private val targets = NameSearchableList<Target>()

    fun clear() {
        targets.clear()
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
        this.targets.addAll(targets)
    }

    fun removeTarget(target: Target) {
        targets.remove(target)
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    fun targetExists(name: String): Boolean {
        return targets.exists(name)
    }

    fun targetExists(target: Target): Boolean {
        return targets.exists(target)
    }

    fun getTarget(name: String): Target? {
        return targets.getAll(name).firstOrNull()
    }

    fun getTargetIncludingPlayerInventory(name: String): Target? {
        return GameState.player.creature.inventory.getItem(name) ?: ScopeManager.getScope().getTarget(name)
    }

    fun getCreature(name: String): Creature? {
        return targets.getAll(name).asSequence().filter { it is Creature }.firstOrNull() as Creature?
    }

    fun getActivator(name: String): Activator? {
        return targets.getAll(name).asSequence().filter { it is Activator }.firstOrNull() as Activator?
    }

    fun getItem(name: String): Item? {
        return targets.getAll(name).asSequence().filter { it is Item }.firstOrNull() as Item?
    }

    fun findTargetsByTag(tag: String): List<Target> {
        return targets.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): List<Activator> {
        return findTargetsByTag(tag).asSequence().filter { it is Activator }.map { it as Activator }.toList()
    }

    fun findActivatorsByProperties(properties: Properties): List<Activator> {
        return targets.asSequence().filter { it is Activator && it.properties.hasAll(properties) }.map { it as Activator }.toList()
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