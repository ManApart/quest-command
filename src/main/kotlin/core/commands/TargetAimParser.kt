package core.commands

import combat.battle.position.TargetAim
import core.gameState.GameState
import core.gameState.Target
import core.gameState.body.BodyPart
import core.history.display
import interact.scope.ScopeManager

fun parseTargets(commandPrefix: String, arguments: List<String>): List<TargetAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    return args.argGroups.mapNotNull { getTarget(it) }
}

private fun getTarget(arguments: List<String>): TargetAim? {
    val args = Args(arguments, delimiters = listOf("of"))
    return when (args.argGroups.size) {
        0 -> null
        1 -> parseTargetOnly(args.fullString)
        2 -> parseTargetAndParts(args)
        else -> {
            display("Could not parse targets for: ${arguments.joinToString(" ")}")
            null
        }
    }
}

private fun parseTargetOnly(name: String): TargetAim? {
    val target = parseTarget(name)
    return if (target != null) {
        TargetAim(target)
    } else {
        null
    }
}

private fun parseTargetAndParts(args: Args): TargetAim? {
    val target = parseTarget(args.getGroupString(1))
    if (target != null) {
        val parts = parseParts(target, args.getGroup(0))
        return TargetAim(target, parts)
    }
    return null
}

private fun parseTarget(name: String): Target? {
    val targets = ScopeManager.getScope(GameState.player.location).getTargets(name)
    //TODO - clarify if too many or too few targets
    return targets.firstOrNull()
}

private fun parseParts(target: Target, names: List<String>): List<BodyPart> {
    if (names.size == 1 && names.first().toLowerCase() == "all"){
        return target.body.getParts()
    }
    return target.body.getAnyParts(names)
}