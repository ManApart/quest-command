package explore.look

import core.history.display
import core.target.Target

fun describeTarget(target: Target) {
    var message = target.getDisplayName()
    message += "\n\t${target.getDescription()}"
    message += describeStatusEffects(target)
    message += describeWeight(target)
    message += describeProperties(target)
    display(message)
}

private fun describeStatusEffects(target: Target): String {
    if (target.soul.getConditions().isNotEmpty()) {
        val effects = target.soul.getConditions().joinToString(", ") { it.name }
        return "\n\t${target.name} is $effects"
    }
    return ""
}

private fun describeProperties(target: Target): String {
    if (!target.properties.isEmpty()) {
        return "\n\t${target.properties}"
    }
    return ""
}

private fun describeWeight(target: Target): String {
    return if (target.getWeight() > 0) {
        "\n\tWeight: ${target.getWeight()}"
    } else {
        ""
    }
}