package core.utility

import core.target.Target


fun Boolean.then(trueChoice: String, falseChoice: String): String {
    return if (this) trueChoice else falseChoice
}

fun Target.asSubject(): String {
    return isPlayer().then("You", name)
}

fun Target.asSubjectPossessive(): String {
    return isPlayer().then("Your", "$name's")
}

fun Target.isAre(): String {
    return isPlayer().then("are", "is")
}