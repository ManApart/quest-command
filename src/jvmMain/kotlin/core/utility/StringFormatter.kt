package core.utility

import core.Player
import core.thing.Thing


fun Boolean.then(trueChoice: String, falseChoice: String): String {
    return if (this) trueChoice else falseChoice
}

/**
 * Returns You if you are the listener, otherwise the thing's name
 */
fun Player.asSubject(listener: Player) = thing.asSubject(listener)
fun Thing.asSubject(listener: Player): String {
    return (this === listener.thing).then("You", name)
}

/**
 * Returns S on the end if you are NOT the subject, otherwise without the s (bob travels, you travel)
 */
fun Player.withS(word: String, listener: Player) = thing.withS(word, listener)
fun Thing.withS(word: String, listener: Player): String {
    return (this === listener.thing).then(word, "${word}s")
}

/**
 * Returns Your if you are the listener, otherwise the thing's name as a possessive
 */
fun Player.asSubjectPossessive(listener: Player) = thing.asSubjectPossessive(listener)
fun Thing.asSubjectPossessive(listener: Player): String {
    return (this === listener.thing).then("Your", "$name's")
}

/**
 * Returns are if you are the listener, otherwise is
 */
fun Player.isAre(listener: Player) = thing.isAre(listener)
fun Thing.isAre(listener: Player): String {
    return (this === listener.thing).then("are", "is")
}

/**
 * Returns youWord if you are the listener, otherwise notYouWord
 */
fun Player.ifYouWord(listener: Player, youWord: String, notYouWord: String) = thing.ifYouWord(listener, youWord, notYouWord)
fun Thing.ifYouWord(listener: Player, youWord: String, notYouWord: String): String {
    return (this === listener.thing).then(youWord, notYouWord)
}