package core.gameState

import core.utility.Named

interface Target : Named {
    val description: String
    val properties: Properties
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
