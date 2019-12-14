package status.effects

import core.body.BodyPart
import core.utility.NameSearchableList
import core.DependencyInjector

object EffectManager {
    private var effects = loadEffects()

    fun reset() {
        effects = loadEffects()
    }

    private fun loadEffects(): NameSearchableList<EffectBase> {
        val parser = DependencyInjector.getImplementation(EffectParser::class.java)
        return parser.loadEffects()
    }

    // should effects be parsable from json as well as effect bases?
    fun getEffect(baseName: String, amount: Int, duration: Int, bodyPartTargets: List<BodyPart> = listOf()) : Effect {
        return Effect(effects.get(baseName), amount, duration, bodyPartTargets)
    }

    fun effectBaseExists(name: String): Boolean {
        return effects.exists(name)
    }


}