package core.commands

import core.Player
import core.history.displayToMe
import core.thing.Thing
import core.utility.Named
import core.utility.capitalize2
import core.utility.getByName
import traveling.position.NO_VECTOR

fun <E : Named> List<E>.pickThing(source: Player, verb: String, name: String? = null, nth: Int? = null): E? {
    return pickThing(source, "${verb.capitalize2()} which?", { item -> "$verb $item" }, name, nth)
}

fun <E : Named> List<E>.pickThing(source: Player, message: String, cmd: (String) -> String, name: String? = null, nth: Int? = null): E? {
    val byName = getByName()
    val first = byName.entries.firstOrNull()?.value
    val n = nth?.let { it - 1 }
    when {
        byName.isEmpty() && name != null -> return null.also { source.displayToMe("Couldn't find $name.") }
        byName.isEmpty() -> return null.also { source.displayToMe("Nothing found.") }

        byName.size == 1 && first?.size == 1 -> return first.first()
        byName.size == 1 && n != null && (first?.size ?: 0) > n -> return first!![n]

        else -> {
            source.respond("Nothing found.") {
                message(message)
                displayedOptions(byName.flatMap { (_, items) ->
                    if (items.size == 1) items.map { it.name } else {
                        items.mapIndexed { i, item ->
                            val desc = when {
                                item !is Thing -> "$name ${i + 1}"
                                item.properties.values.getString("locationDescription").isNotBlank() -> item.getDisplayName()
                                item.position != NO_VECTOR -> item.getDisplayName()
                                else -> "$name ${i + 1}"
                            }
                            cmd(desc)
                        }
                    }
                })
                options(byName.flatMap { (_, items) ->
                    if (items.size == 1) items.map { cmd(it.name) } else {
                        items.mapIndexed { i, item ->
                            cmd("${item.name} ${i + 1}")
                        }
                    }
                })
            }
        }
    }
    return null
}
