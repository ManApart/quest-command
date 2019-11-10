package core.utility

import core.gameState.GameState
import system.debug.DebugType

object RandomManager {

    fun isSuccess(chance: Double) : Boolean {
        if (GameState.properties.values.getBoolean(DebugType.RANDOM.propertyName)){
            return true
        }

        if (chance <= 0){
            return false
        }

        val rand = Math.random()
//        println("$chance, $rand")
        return chance >= rand
    }
}