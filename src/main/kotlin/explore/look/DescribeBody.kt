package explore.look

import core.Player
import core.body.Body
import core.history.StringTable
import core.history.displayToMe
import traveling.location.RouteNeighborFinder

fun describeBody(source: Player, body: Body) {
    val routes = RouteNeighborFinder(body.layout.rootNode, 100).getNeighbors()

    val routeTable = if (routes.isNotEmpty()) {
        val input = mutableListOf(listOf("Name", "Distance", "Direction Path"))
        input.addAll(routes.map { it.getRouteString() })
        val table = StringTable(input, 2, rightPadding = 2)

        table.getString()
    } else ""

    source.displayToMe("${body.name} body:\n$routeTable")
}

fun describeBodyDetailed(source: Player, body: Body) {
    describeBody(source, body)
}
