package core.utility

import core.GameState
import system.debug.DebugType

object RandomManager {

    /**
     * Expects 0-100
     */
    fun isSuccess(chance: Int): Boolean {
        val input: Double = chance/100.toDouble()
        return isSuccess(input)
    }

    /**
     * Expects 0.0 - 1.0
     */
    fun isSuccess(chance: Double): Boolean {
        if (GameState.properties.values.getBoolean(DebugType.RANDOM.propertyName)) {
            return true
        }

        if (chance <= 0) {
            return false
        }

        val rand = Math.random()
//        println("$chance, $rand")
        return chance >= rand
    }
}