package core

import core.ai.AIJsonParser
import core.ai.AIParser
import core.ai.behavior.BehaviorJsonParser
import core.ai.behavior.BehaviorParser
import core.body.BodyJsonParser
import core.body.BodyParser
import core.reflection.GeneratedReflections
import core.reflection.Reflections
import core.target.activator.ActivatorJsonParser
import core.target.activator.ActivatorParser
import core.target.creature.CreatureJsonParser
import core.target.creature.CreatureParser
import core.target.item.ItemJsonParser
import core.target.item.ItemParser
import core.utility.KotlinResourceHelper
import core.utility.ResourceHelper
import crafting.RecipeJsonParser
import crafting.RecipeParser
import quests.QuestJsonParser
import quests.QuestParser
import status.effects.EffectJsonParser
import status.effects.EffectParser
import traveling.location.LocationJsonParser
import traveling.location.LocationParser

object DependencyInjector {
    private val interfaces = mutableMapOf<Class<*>, Any>()
    private val defaultImplementations by lazy { createDefaultImplementations() }


    fun <T : E, E : Any> setImplementation(targetInterface: Class<E>, implementation: T) {
        interfaces[targetInterface] = implementation
    }

    fun <E : Any> clearImplementation(targetInterface: Class<E>) {
        interfaces.remove(targetInterface)
    }

    fun <T : E, E : Any> getImplementation(targetInterface: Class<E>): T {
        return when {
            interfaces.containsKey(targetInterface) -> {
                @Suppress("UNCHECKED_CAST")
                interfaces[targetInterface] as T
            }
            defaultImplementations.containsKey(targetInterface) -> {
                @Suppress("UNCHECKED_CAST")
                defaultImplementations[targetInterface] as T
            }
            else -> throw IllegalArgumentException("No implementation could be found for interface ${targetInterface.simpleName}")
        }
    }

    private fun createDefaultImplementations(): Map<Class<*>, Any> {
        return mapOf(
                ActivatorParser::class.java to ActivatorJsonParser(),
                AIParser::class.java to AIJsonParser(),
                BehaviorParser::class.java to BehaviorJsonParser(),
                BodyParser::class.java to BodyJsonParser(),
                CreatureParser::class.java to CreatureJsonParser(),
                EffectParser::class.java to EffectJsonParser(),
                ItemParser::class.java to ItemJsonParser(),
                LocationParser::class.java to LocationJsonParser(),
                QuestParser::class.java to QuestJsonParser(),
                RecipeParser::class.java to RecipeJsonParser(),
                Reflections::class.java to GeneratedReflections(),
                ResourceHelper::class.java to KotlinResourceHelper()
        )
    }


}