package core.commands

import traveling.direction.Direction
import traveling.position.Vector

private val commaSeparateDigits = Regex("\\d,\\d,\\d")

fun parseDirection(arguments: List<String>): Direction {
    return parseNullableDirection(arguments) ?: Direction.NONE
}

fun parseNullableDirection(arguments: List<String>): Direction? {
    val directionArg = arguments.firstOrNull {
        Direction.getDirection(it) != Direction.NONE
    }

    return if (directionArg == null) {
        null
    } else {
        Direction.getDirection(directionArg)
    }
}

fun parseVector(arguments: List<String>, default: Vector = Vector()): Vector {
    val args = Args(arguments, listOf(ArgDelimiter(listOf(",", " "))))
    val numbers = args.getNumbers(",", true)
    return when {
        numbers.size == 3 && numbers.all { it != null } -> Vector(numbers[0]!!, numbers[1]!!, numbers[2]!!)
        else -> default
    }
}

