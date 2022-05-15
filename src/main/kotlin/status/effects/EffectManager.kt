package status.effects

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.location.Location

object EffectManager {
    private var effects = loadEffects()

    fun reset() {
        effects = loadEffects()
    }

    private fun loadEffects(): NameSearchableList<EffectBase> {
        startupLog("Loading Effects")
        val parser = DependencyInjector.getImplementation(EffectsCollection::class)
        return parser.values.toNameSearchableList()
    }

    // should effects be parsable from json as well as effect bases?
    fun getEffect(baseName: String, amount: Int, duration: Int, bodyPartThings: List<Location> = listOf()) : Effect {
        return Effect(effects.get(baseName), amount, duration, bodyPartThings)
    }

    fun effectBaseExists(name: String): Boolean {
        return effects.exists(name)
    }


}