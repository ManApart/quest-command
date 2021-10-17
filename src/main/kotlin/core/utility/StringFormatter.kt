package core.utility

import core.Player
import core.target.Target


fun Boolean.then(trueChoice: String, falseChoice: String): String {
    return if (this) trueChoice else falseChoice
}

/**
 * Returns You if you are the listener, otherwise the target's name
 */
fun Target.asSubject(listener: Player): String {
    return (this === listener.target).then("You", name)
}

fun Player.asSubject(listener: Player): String {
    return target.asSubject(listener)
}

/**
 * Returns Your if you are the listener, otherwise the target's name as a possessive
 */
fun Target.asSubjectPossessive(listener: Player): String {
    return (this === listener.target).then("Your", "$name's")
}

fun Player.asSubjectPossessive(listener: Player): String {
    return target.asSubjectPossessive(listener)
}

/**
 * Returns are if you are the listener, otherwise is
 */
fun Target.isAre(listener: Player): String {
    return (this === listener.target).then("are", "is")
}

fun Player.isAre(listener: Player): String {
    return target.isAre(listener)
}