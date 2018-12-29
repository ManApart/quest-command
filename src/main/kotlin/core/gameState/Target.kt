package core.gameState

import core.events.Event
import core.utility.Named

interface Target : Named {
    val description: String
    val properties: Properties

    fun canConsume(event: Event) : Boolean {
        return if (this is Activator) {
            this.evaluate(event)
        } else if (this is Creature && this.parent is Activator) {
            this.parent.evaluate(event)
        } else if (this is Item) {
            this.evaluate(event)
        } else {
            false
        }
    }

    fun consume(event: Event) {
        if (this is Activator) {
            this.evaluateAndExecute(event)
        } else if (this is Creature && this.parent is Activator) {
            this.parent.evaluateAndExecute(event)
        } else if (this is Item) {
            this.evaluateAndExecute(event)
        }
    }
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
