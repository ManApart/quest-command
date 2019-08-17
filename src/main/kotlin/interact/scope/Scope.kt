package interact.scope

import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class Scope(private val locationNode: LocationNode) {
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
        if (GameState.player.inventory.exists(target)) {
            GameState.player.inventory.remove(target)
        } else {
            targets.remove(target)
        }
    }

    fun getTargets(): List<Target> {
        return targets.toList()
    }

    fun getTargets(name: String): List<Target> {
        return targets.getAll(name)
    }

    fun getTargetsIncludingPlayerInventory(): List<Target> {
        return GameState.player.inventory.getItems() + getTargets()
    }

    fun getTargetsIncludingPlayerInventory(name: String): List<Target> {
        return GameState.player.inventory.getItems(name) + getTargets(name)
    }

    fun getCreatures(): List<Target> {
        return targets.asSequence().filter { it.properties.isCreature() }.toList()
    }

    fun getCreatures(name: String): List<Target> {
        return targets.getAll(name).asSequence().filter { it.properties.isCreature() }.toList()
    }

    fun getActivators(name: String): List<Target> {
        return targets.getAll(name).asSequence().filter { it.properties.isActivator() }.toList()
    }

    fun getActivators(): List<Target> {
        return targets.asSequence().filter { it.properties.isActivator() }.toList()
    }

    fun getItems(): List<Target> {
        return targets.asSequence().filter { it.properties.isItem() }.toList()
    }

    fun getItems(name: String): List<Target> {
        return targets.getAll(name).asSequence().filter { it.properties.isItem() }.toList()
    }

    fun getItemsIncludingPlayerInventory(): List<Target> {
        return GameState.player.inventory.getItems() + getItems()
    }

    fun getItemsIncludingPlayerInventory(name: String): List<Target> {
        return GameState.player.inventory.getItems(name) + getItems(name)
    }

    fun findTargetsByTag(tag: String): List<Target> {
        return targets.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): List<Target> {
        return findTargetsByTag(tag).asSequence().filter { it.properties.isActivator() }.toList()
    }

    fun findActivatorsByProperties(properties: Properties): List<Target> {
        return targets.asSequence().filter { it.properties.isActivator() && it.properties.hasAll(properties) }.toList()
    }

    fun getAllSouls(): List<Soul> {
        val souls = mutableListOf<Soul?>()
        souls.add(GameState.player.soul)
        souls.addAll(GameState.player.inventory.getAllItems().map { it.soul })

        targets.forEach {
            souls.add(it.soul)
            it.inventory.getAllItems().forEach { item -> souls.add(item.soul) }
        }
        return souls.filterNotNull()
    }

    fun getAllInventories(): List<Inventory> {
        return targets.asSequence().map { it.inventory }.toList()
    }

}