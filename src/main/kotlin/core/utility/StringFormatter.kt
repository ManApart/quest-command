package core.utility

import core.target.Target

object StringFormatter {

    fun format(value: Boolean, trueChoice: String, falseChoice: String): String {
        return if (value) trueChoice else falseChoice
    }

    fun getSubject(target: Target): String {
        return format(target.isPlayer(), "You", target.name)
    }

    fun getSubjectPossessive(target: Target): String {
        return format(target.isPlayer(), "Your", target.name + "'s")
    }

    fun getIsAre(target: Target): String {
        return format(target.isPlayer(), "are", "is")
    }


//    fun joinWithAnd(targets: List<Target>) : String {
//        return targets.joinToString(", ") { it.name }
//    }
}