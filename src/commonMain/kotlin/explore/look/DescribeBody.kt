package explore.look

import core.Player
import core.body.NONE
import core.history.StringTable
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import crafting.material.DEFAULT_MATERIAL
import traveling.location.RouteNeighborFinder

fun describeBody(source: Player, thing: Thing) {
    val body = thing.body
    val routes = RouteNeighborFinder(body.layout.rootNode, 100).getNeighbors()

    val routeTable = if (routes.isNotEmpty()) {
        val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
        input.add(listOf(body.layout.rootNode.name, "0", "N/A"))
        input.addAll(routes.map { it.getRouteString() })
        val table = StringTable(input, 2, rightPadding = 2)

        table.getString()
    } else ""

    if (body.name == NONE.name) {
        val materialString = if (body.material.name != DEFAULT_MATERIAL.name) " It is made of ${body.material.name}." else ""
        source.displayToMe("This has no body.$materialString")
    } else {
        source.displayToMe("${body.name} body:\n$routeTable")
    }
    source.displayToOthers("${source.name} looks at ${thing.name}'s body.")
}

fun describeBodyDetailed(source: Player, thing: Thing) {
    describeBody(source, thing)
    source.displayToOthers("${source.name} looks at ${thing.name}'s body.")
}
