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
    val args = Args(arguments, listOf(ArgDelimiter(listOf(",", " "))))
    val numbers = args.getNumbers(",", true)
    return when {
        numbers.size == 3 && numbers.all { it != null } -> Vector(numbers[0]!!, numbers[1]!!, numbers[2]!!)
        else -> Vector()
    }
}

