package interact.scope

import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class Scope(val locationNode: LocationNode) {
    private val targets = NameSearchableList<Target>()

    fun isEmpty(): Boolean {
        return targets.isEmpty()
    }

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

    fun removeTargetIncludingPlayerInventory(target: Target) {
        if (target is Item && GameState.player.creature.inventory.exists(target)) {
            GameState.player.creature.inventory.remove(target)
        } else {
            targets.remove(target)
        }
    }

    fun getAllTargets(): List<Target> {
        return targets.toList()
    }

    fun getTargets(name: String): List<Target> {
        return targets.getAll(name)
    }

    fun getTargetsIncludingPlayerInventory(name: String): List<Target> {
        return (listOf(GameState.player.creature.inventory.getItem(name)) + getTargets(name)).filterNotNull()
    }

    fun getCreatures(name: String): List<Creature> {
        return targets.getAll(name).asSequence().filter { it is Creature }.map { it as Creature }.toList()
    }

    fun getActivators(name: String): List<Activator> {
        return targets.getAll(name).asSequence().filter { it is Activator }.map { it as Activator }.toList()
    }

    fun getItems(name: String): List<Item> {
        return targets.getAll(name).asSequence().filter { it is Item }.map { it as Item }.toList()
    }

    fun getItemsIncludingPlayerInventory(name: String): List<Item> {
        return GameState.player.creature.inventory.getItems(name) + getItems(name)
    }

    fun getTargetsWithCreatures(name: String): List<Creature> {
        return getTargets(name).asSequence()
                .map { it.getCreature() }.toList()
                .filterNotNull()
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
        val souls = mutableListOf<Soul?>()
        souls.add(GameState.player.creature.soul)
        souls.addAll(GameState.player.creature.inventory.getAllItems().map { it.soul })

        targets.forEach {
            souls.add(it.getCreature()?.soul)
            it.getCreature()?.inventory?.getAllItems()?.forEach { item -> souls.add(item.soul) }
        }

        return souls.filterNotNull()
    }

    fun getAllInventories(): List<Inventory> {
        return targets.asSequence()
                .map { it.getCreature()?.inventory }.toList()
                .filterNotNull()
    }

}