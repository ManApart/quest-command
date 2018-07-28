package core.utility

import core.gameState.GameState
import core.gameState.Target

object StringFormatter {

    fun format(value: Boolean, trueChoice: String, falseChoice: String) : String{
        return if(value) trueChoice else falseChoice
    }

    fun getSubject(target: Target) : String {
        return format(isPlayer(target), "You", target.name)
    }

    fun getSubjectPossessive(target: Target) : String {
        return format(isPlayer(target), "Your", target.name + "'s")
    }

    private fun isPlayer(target: Target) : Boolean {
        return target == GameState.player || target == GameState.player.creature
    }

//    fun joinWithAnd(targets: List<Target>) : String {
//        return targets.joinToString(", ") { it.name }
//    }
}