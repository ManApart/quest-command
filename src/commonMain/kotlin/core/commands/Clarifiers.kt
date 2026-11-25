package core.commands

import core.Player
import core.history.displayToMe
import core.thing.Thing
import core.utility.Named
import core.utility.getByName
import traveling.position.NO_VECTOR

fun <E : Named> List<E>.pickItem(source: Player, name: String, verb: String, nth: Int? = null, onFind: (E) -> Unit) {
    val byName = getByName()
    val first = byName.entries.firstOrNull()?.value
    val n = nth?.let { it - 1 }
    when {
        byName.isEmpty() -> source.displayToMe("Couldn't find $name.")
        byName.size == 1 && first?.size == 1 -> onFind(first.first())
        byName.size == 1 && n != null && (first?.size ?: 0) > n -> onFind(first!![n])

        else -> {
            source.respond("There is nothing for you to $verb.") {
                message("${verb.capitalize()} which?")
                displayedOptions(byName.flatMap { (_, items) ->
                    if (items.size == 1) items.map { it.name } else {
                        items.mapIndexed { i, item ->
                            val desc = when {
                                item !is Thing -> "$name ${i + 1}"
                                item.properties.values.getString("locationDescription").isNotBlank() -> item.getDisplayName()
                                item.position != NO_VECTOR -> item.getDisplayName()
                                else -> "$name ${i + 1}"
                            }
                            "$verb $desc"
                        }
                    }
                })
                options(byName.flatMap { (_, items) ->
                    if (items.size == 1) items.map { it.name } else {
                        items.mapIndexed { i, item ->
                            "$verb ${item.name} ${i + 1}"
                        }
                    }
                })
            }
        }
    }
}
