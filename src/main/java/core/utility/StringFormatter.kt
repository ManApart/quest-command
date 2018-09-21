package core.utility

import core.gameState.Target
import core.gameState.isPlayer

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

//    fun joinWithAnd(targets: List<Target>) : String {
//        return targets.joinToString(", ") { it.name }
//    }
}