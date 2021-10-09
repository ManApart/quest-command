package core.utility

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

/**
 * Returns Your if you are the listener, otherwise the target's name as a possessive
 */
fun Target.asSubjectPossessive(listener: Target): String {
    return (this === listener).then("Your", "$name's")
}

/**
 * Returns are if you are the listener, otherwise is
 */
fun Target.isAre(listener: Target): String {
    return (this === listener).then("are", "is")
}