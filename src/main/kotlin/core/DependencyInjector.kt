package core

import core.ai.AIJsonParser
import core.ai.AIParser
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsGenerated
import core.commands.CommandsCollection
import core.commands.CommandsGenerated
import core.events.EventListenersCollection
import core.events.EventListenersGenerated
import core.events.eventParsers.EventParsersCollection
import core.events.eventParsers.EventParsersGenerated
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
import magic.spellCommands.SpellCommandsGenerated
import magic.spellCommands.SpellCommandsCollection
import quests.StoryEventsCollection
import quests.StoryEventsGenerated
import status.conditions.ConditionJsonParser
import status.conditions.ConditionParser
import status.effects.EffectJsonParser
import status.effects.EffectParser
import traveling.location.location.LocationJsonParser
import traveling.location.location.LocationParser
import traveling.location.weather.WeatherJsonParser
import traveling.location.weather.WeatherParser

object DependencyInjector {
    private val interfaces = mutableMapOf<Class<*>, Any>()
    private val defaultImplementations by lazy { createDefaultImplementations() }


    fun <T : E, E : Any> setImplementation(targetInterface: Class<E>, implementation: T) {
        interfaces[targetInterface] = implementation
    }

    fun <E : Any> clearImplementation(targetInterface: Class<E>) {
        interfaces.remove(targetInterface)
    }

    fun clearAllImplementations() {
        interfaces.clear()
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
                BehaviorsCollection::class.java to BehaviorsGenerated(),
                CreatureParser::class.java to CreatureJsonParser(),
                ConditionParser::class.java to ConditionJsonParser(),
                CommandsCollection::class.java to CommandsGenerated(),
                EffectParser::class.java to EffectJsonParser(),
                EventParsersCollection::class.java to EventParsersGenerated(),
                EventListenersCollection::class.java to EventListenersGenerated(),
                ItemParser::class.java to ItemJsonParser(),
                LocationParser::class.java to LocationJsonParser(),
                RecipeParser::class.java to RecipeJsonParser(),
                ResourceHelper::class.java to KotlinResourceHelper(),
                SpellCommandsCollection::class.java to SpellCommandsGenerated(),
                StoryEventsCollection::class.java to StoryEventsGenerated(),
                WeatherParser::class.java to WeatherJsonParser(),
        )
    }


}