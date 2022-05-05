package core.utility

import core.Player
import core.thing.Thing


fun Boolean.then(trueChoice: String, falseChoice: String): String {
    return if (this) trueChoice else falseChoice
}

/**
 * Returns You if you are the listener, otherwise the thing's name
 */
fun Thing.asSubject(listener: Player): String {
    return (this === listener.thing).then("You", name)
}

fun Player.asSubject(listener: Player): String {
    return thing.asSubject(listener)
}

/**
 * Returns S on the end if you are NOT the subject, otherwise without the s (bob travels, you travel)
 */
fun Thing.withS(word: String, listener: Player): String {
    return (this === listener.thing).then(word, "${word}s")
}

/**
 * Returns Your if you are the listener, otherwise the thing's name as a possessive
 */
fun Thing.asSubjectPossessive(listener: Player): String {
    return (this === listener.thing).then("Your", "$name's")
}

fun Player.asSubjectPossessive(listener: Player): String {
    return thing.asSubjectPossessive(listener)
}

/**
 * Returns are if you are the listener, otherwise is
 */
fun Thing.isAre(listener: Player): String {
    return (this === listener.thing).then("are", "is")
}

fun Player.isAre(listener: Player): String {
    return thing.isAre(listener)
}