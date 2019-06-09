package system

import core.gameState.quests.QuestJsonParser
import core.gameState.quests.QuestParser
import core.utility.KotlinReflectionParser
import core.utility.KotlinResourceHelper
import core.utility.ReflectionParser
import core.utility.ResourceHelper
import crafting.RecipeJsonParser
import crafting.RecipeParser
import status.effects.EffectJsonParser
import status.effects.EffectParser
import system.activator.ActivatorJsonParser
import system.activator.ActivatorParser
import system.ai.AIJsonParser
import system.ai.AIParser
import system.behavior.BehaviorJsonParser
import system.behavior.BehaviorParser
import system.body.BodyJsonParser
import system.body.BodyParser
import system.creature.CreatureJsonParser
import system.creature.CreatureParser
import system.item.ItemJsonParser
import system.item.ItemParser
import system.location.LocationJsonParser
import system.location.LocationParser

object DependencyInjector {
    private val interfaces = mutableMapOf<Class<*>, Any>()
    private val defaultImplementations = createDefaultImplementations()


    fun <T : E, E : Any> setImplementation(targetInterface: Class<E>, implementation: T) {
        interfaces[targetInterface] = implementation
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
                ReflectionParser::class.java to KotlinReflectionParser(),
                ResourceHelper::class.java to KotlinResourceHelper()
        )
    }


}