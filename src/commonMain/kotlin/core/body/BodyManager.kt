package core.body

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList

object BodyManager {
    private var bodies2 by lazyM { createBodies2() }

    fun reset() {
        bodies2 = createBodies2()
    }

    private fun createBodies2(): NameSearchableList<Body> {
        startupLog("Creating Bodies.")
        return DependencyInjector.getImplementation(BodysCollection::class).values
            .map { it.build() }.toNameSearchableList()
    }

    fun bodyExists(name: String): Boolean {
        return bodies2.firstOrNull { it.name.lowercase() == name.lowercase() } != null
    }

    fun getBody(name: String) = bodies2.get(name).copy()
}
