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
        targetCounts[it.name] = targetCounts[it.name]?.plus(count) ?: count
    }

    return targetCounts.entries.joinToString(", ") {
        if (it.value == 1) {
            it.key
        } else {
            "${it.value}x ${it.key}"
        }
    }
}

fun hasCreature(target: Target) : Boolean {
    return getCreature(target) != null
}

fun getCreature(target: Target) : Creature? {
    return when (target){
        is Creature -> target
        is Activator -> target.creature
        is Player -> target.creature
        else -> null
    }
}

fun hasSoul(target: Target) : Boolean {
    return getSoul(target) != null
}

fun getSoul(target: Target) : Soul? {
    return when (target){
        is Creature -> target.soul
        is Item -> target.soul
        is Activator -> target.creature.soul
        is Player -> target.creature.soul
        else -> null
    }
}

fun isPlayer(target: Target) : Boolean {
    return target == GameState.player || target == GameState.player.creature
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
