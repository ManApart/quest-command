package core.utility

import core.GameState
import system.debug.DebugType

object RandomManager {

    /**
     * Expects 0-100
     */
    fun isSuccess(chance: Int): Boolean {
        val input: Double = chance / 100.toDouble()
        return isSuccess(input)
    }

    /**
     * Expects 0.0 - 1.0
     */
    fun isSuccess(chance: Double): Boolean {
        return when {
            GameState.getDebugBoolean(DebugType.RANDOM_SUCCEED) -> true
            GameState.getDebugBoolean(DebugType.RANDOM_FAIL) -> false
            chance <= 0 -> false
            else -> chance >= Math.random()
        }
    }

    fun <E> getRandom(list: List<E>): E {
        return list[getRandom(0, list.size - 1)]
    }

    fun <E> getRandomOrNull(list: List<E>): E? {
        return if (list.isEmpty()) {
            null
        } else {
            list[getRandom(0, list.size - 1)]
        }
    }

    fun getRandom(min: Int, max: Int): Int {
        if (GameState.properties.values.hasInt(DebugType.RANDOM_RESPONSE.propertyName)) {
            return GameState.properties.values.getInt(DebugType.RANDOM_RESPONSE.propertyName)
        }
        return (min..max).random()
    }

}