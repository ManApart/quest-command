package core.body

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList

object BodyManager {
    private var bodies by lazyM { createBodies() }

    fun reset() {
        bodies = createBodies()
    }

    private fun createBodies(): NameSearchableList<Body> {
        startupLog("Creating Bodies.")
        return DependencyInjector.getImplementation(BodysCollection::class).values
            .map { it.build() }.toNameSearchableList()
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.lowercase() == name.lowercase() } != null
    }

    fun getBody(name: String) = bodies.get(name).copy()
}
