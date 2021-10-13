package core.utility

import core.Player
import core.target.Target


fun Boolean.then(trueChoice: String, falseChoice: String): String {
    return if (this) trueChoice else falseChoice
}

/**
 * Returns You if you are the listener, otherwise the target's name
 */
fun Target.asSubject(listener: Target): String {
    return (this === listener).then("You", name)
}

fun Player.asSubject(listener: Target): String {
    return target.asSubject(listener)
}

/**
 * Returns Your if you are the listener, otherwise the target's name as a possessive
 */
fun Target.asSubjectPossessive(listener: Target): String {
    return (this === listener).then("Your", "$name's")
}

fun Player.asSubjectPossessive(listener: Target): String {
    return target.asSubjectPossessive(listener)
}

/**
 * Returns are if you are the listener, otherwise is
 */
fun Target.isAre(listener: Target): String {
    return (this === listener).then("are", "is")
}

fun Player.isAre(listener: Target): String {
    return target.isAre(listener)
}