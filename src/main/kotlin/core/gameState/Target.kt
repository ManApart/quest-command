package core.gameState

import core.events.Event
import core.gameState.location.LocationNode
import core.utility.Named

interface Target : Named {
    val description: String
    val properties: Properties
    val inventory: Inventory
    val location: LocationNode

    fun canConsume(event: Event) : Boolean
    fun consume(event: Event)

}

fun targetsToString(targets: List<Target>) : String {
    val targetCounts = HashMap<String, Int>()
    targets.forEach {
        val count = (it as? Item)?.count ?: 1
        targetCounts[it.getDisplayName()] = targetCounts[it.getDisplayName()]?.plus(count) ?: count
    }

    return targetCounts.entries.joinToString(", ") {
        if (it.value == 1) {
            it.key
        } else {
            "${it.value}x ${it.key}"
        }
    }
}

fun Target.getDisplayName() : String {
    val locationDescription = properties.values.getString("locationDescription")
    return name + if (locationDescription.isBlank()){
        ""
    } else {
        " $locationDescription"
    }
}

fun Target.hasCreature() : Boolean {
    return getCreature() != null
}

fun Target.getCreature() : Creature? {
    return when (this){
        is Creature -> this
        is Activator -> this.creature
        is Player -> this.creature
        else -> null
    }
}

fun Target.hasSoul() : Boolean {
    return getSoul() != null
}

fun Target.getSoul() : Soul? {
    return when (this){
        is Creature -> this.soul
        is Item -> this.soul
        is Activator -> this.creature.soul
        is Player -> this.creature.soul
        else -> null
    }
}

fun Target.isPlayer() : Boolean {
    return this == GameState.player || this == GameState.player.creature
}

fun Target.getTopParent() : Target {
    return when (this){
        is Creature -> this.parent
        is Item -> this
        is Activator -> this
        is Player -> this
        else -> this
    } ?: this
}
