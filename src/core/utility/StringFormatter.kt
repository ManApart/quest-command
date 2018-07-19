package core.utility

import core.gameState.GameState
import core.gameState.Target

object StringFormatter {

    fun format(value: Boolean, trueChoice: String, falseChoice: String) : String{
        return if(value) trueChoice else falseChoice
    }

    fun getSubject(target: Target) : String {
        return format(target == GameState.player, "You", target.name)
    }

    fun getSubjectPossessive(target: Target) : String {
        return format(target == GameState.player, "Your", target.name + "'s")
    }
}