package core.commands

import traveling.position.TargetAim
import core.GameState
import core.target.Target
import core.history.display
import core.utility.NameSearchableList
import traveling.location.location.Location

//TODO - allow for response requests?
fun parseTargetsFromInventory(arguments: List<String>, target: Target = GameState.player): List<TargetAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    val targets = NameSearchableList(target.inventory.getAllItems())
    return args.getBaseAndGroups("and").mapNotNull { getTarget(it, targets) }
}

//TODO - make location paramatized
fun parseTargets(arguments: List<String>): List<TargetAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    val targets = GameState.currentLocation().getTargets()
    return args.getBaseAndGroups("and").mapNotNull { getTarget(it, targets) }
}

private fun getTarget(arguments: List<String>, targets: NameSearchableList<Target>): TargetAim? {
    val args = Args(arguments, delimiters = listOf("of"))
    return when {
        args.hasBase() && args.hasGroup("of") -> parseTargetAndParts(args, targets)
        args.hasBase() -> parseTargetOnly(args.fullString, targets)
        else -> {
            display("Could not parse targets for: ${arguments.joinToString(" ")}")
            null
        }
    }
}

private fun parseTargetOnly(name: String, targets: NameSearchableList<Target>): TargetAim? {
    val target = parseTarget(name, targets)
    return if (target != null) {
        TargetAim(target)
    } else {
        null
    }
}

private fun parseTargetAndParts(args: Args, targets: NameSearchableList<Target>): TargetAim? {
    val target = parseTarget(args.getString("of"), targets)
    if (target != null) {
        val parts = parseBodyParts(target, args.getGroup("base"))
        return TargetAim(target, parts)
    }
    return null
}

private fun parseTarget(name: String, targets: NameSearchableList<Target>): Target? {
    //TODO - clarify if too many or too few targets
    return targets.getOrNull(name)
}

fun parseBodyParts(target: Target, names: List<String>): List<Location> {
    if (names.size == 1 && names.first().lowercase() == "all") {
        return target.body.getParts()
    }
    return target.body.getAnyParts(names)
}