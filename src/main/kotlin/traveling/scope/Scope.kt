package traveling.scope

import core.GameState
import core.properties.Properties
import status.Soul
import core.target.Target
import traveling.location.LocationNode
import core.utility.NameSearchableList
import core.utility.plus
import inventory.Inventory

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

    fun getTargets(source: Target = GameState.player): NameSearchableList<Target> {
        return targets.sortedBy { source.position.getDistance(it.position) }
    }

    fun getTargets(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return when {
            targets.existsExact(name) && targets.countExact(name) == 1 -> NameSearchableList(targets.get(name))
            targets.existsByWholeWord(name) && targets.countByWholeWord(name) == 1 -> NameSearchableList(targets.get(name))
            else -> targets.getAll(name).sortedBy { source.position.getDistance(it.position) }
        }
    }

    fun getTargetsIncludingPlayerInventory(source: Target = GameState.player): List<Target> {
        return GameState.player.inventory.getItems() + getTargets(source)
    }

    fun getTargetsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getTargets(name, source)
    }

    fun getCreatures(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isCreature() }
    }

    fun getCreatures(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isCreature() }
    }

    fun getActivators(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isActivator() }
    }

    fun getActivators(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isActivator() }
    }

    fun getItems(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isItem() }
    }

    fun getItems(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isItem() }
    }

    fun getItemsIncludingPlayerInventory(source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems() + getItems(source)
    }

    fun getItemsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getItems(name, source)
    }

    fun findTargetsByTag(tag: String): NameSearchableList<Target> {
        return targets.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): NameSearchableList<Target> {
        return findTargetsByTag(tag).filter { it.properties.isActivator() }
    }

    fun findActivatorsByProperties(properties: Properties): NameSearchableList<Target> {
        return targets.filter { it.properties.isActivator() && it.properties.hasAll(properties) }
    }

    fun getAllSouls(): List<Soul> {
        val souls = mutableListOf<Soul?>()
        souls.addAll(GameState.player.inventory.getAllItems().map { it.soul })

        targets.forEach {
            souls.add(it.soul)
            it.inventory.getAllItems().forEach { item -> souls.add(item.soul) }
        }

        if (!souls.contains(GameState.player.soul)) {
            souls.add(GameState.player.soul)
        }
        return souls.filterNotNull()
    }

    fun getAllInventories(): List<Inventory> {
        return targets.asSequence().map { it.inventory }.toList()
    }

}
