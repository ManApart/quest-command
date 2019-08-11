package status.effects

import core.gameState.body.BodyPart
import core.utility.NameSearchableList
import system.DependencyInjector

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
    fun getEffect(baseName: String, amount: Int = 1, target: BodyPart? = null) : Effect {
        return Effect(effects.get(baseName), amount, target)
    }

    fun effectExists(name: String): Boolean {
        return effects.exists(name)
    }


}