package core.commands

import core.gameState.Direction
import core.gameState.Vector

private val commaSeparateDigits = Regex("\\d,\\d,\\d")

fun parseDirection(arguments: List<String>): Direction {
    val directionArg = arguments.firstOrNull {
        Direction.getDirection(it) != Direction.NONE
    }

    return Direction.getDirection(directionArg ?: Direction.NONE.name)
}

fun parseVector(arguments: List<String>): Vector {
    val args = Args(arguments)
    return when {
        isSpaceSeparatedPosition(args) -> Vector(args.getNumber(0), args.getNumber(1), args.getNumber(2))
        isCommaSeparatedPosition(args) -> parseCommaVector(args)
        else -> Vector()
    }
}

fun isCommaSeparatedPosition(args: Args): Boolean {
    return args.has(commaSeparateDigits).isNotEmpty()
}

fun isSpaceSeparatedPosition(args: Args): Boolean {
    return args.args.size == 3 && args.args.all { it.toIntOrNull() != null }
}

fun parseCommaVector(args: Args): Vector {
    val coords = args.has(commaSeparateDigits).first().split(",").map { it.toInt() }
    return Vector(coords[0], coords[1], coords[2])
}

