package core.gameState

interface Target {
    val name: String
    val tags : Tags
}

fun targetsToString(targets: List<Target>) : String {
    val targetCounts = HashMap<String, Int>()
    targets.forEach {
        targetCounts[it.name] = targetCounts[it.name]?.plus(1) ?: 1
    }

    return targetCounts.entries.joinToString(", ") {
        if (it.value == 1) {
            it.key
        } else {
            "${it.value}x ${it.key}"
        }
    }
}