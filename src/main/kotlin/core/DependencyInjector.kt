package core

import conversation.dsl.DialoguesCollection
import conversation.dsl.DialoguesGenerated
import core.ai.action.dsl.AIActionsCollection
import core.ai.action.dsl.AIActionsGenerated
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsGenerated
import core.ai.dsl.AIsCollection
import core.ai.dsl.AIsGenerated
import core.commands.CommandsCollection
import core.commands.CommandsGenerated
import core.events.EventListener
import core.events.EventListenersCollection
import core.events.EventListenersGenerated
import core.target.activator.dsl.ActivatorsCollection
import core.target.activator.dsl.ActivatorsGenerated
import core.target.creature.CreaturesCollection
import core.target.creature.CreaturesGenerated
import core.target.item.ItemsCollection
import core.target.item.ItemsGenerated
import core.utility.KotlinResourceHelper
import core.utility.ResourceHelper
import crafting.RecipesCollection
import crafting.RecipesGenerated
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsGenerated
import quests.StoryEventsCollection
import quests.StoryEventsGenerated
import status.conditions.ConditionsCollection
import status.conditions.ConditionsGenerated
import status.effects.EffectsCollection
import status.effects.EffectsGenerated
import traveling.location.location.LocationDescriptionsCollection
import traveling.location.location.LocationDescriptionsGenerated
import traveling.location.location.LocationJsonParser
import traveling.location.location.LocationParser
import traveling.location.weather.*

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

    inline fun <reified T : EventListener<*>> getListener(): T {
        return getImplementation(EventListenersCollection::class.java).values.first { it::class == T::class } as T
    }

    private fun createDefaultImplementations(): Map<Class<*>, Any> {
        return mapOf(
            ActivatorsCollection::class.java to ActivatorsGenerated(),
            AIsCollection::class.java to AIsGenerated(),
            AIActionsCollection::class.java to AIActionsGenerated(),
            BehaviorsCollection::class.java to BehaviorsGenerated(),
            CreaturesCollection::class.java to CreaturesGenerated(),
            ConditionsCollection::class.java to ConditionsGenerated(),
            CommandsCollection::class.java to CommandsGenerated(),
            DialoguesCollection::class.java to DialoguesGenerated(),
            EffectsCollection::class.java to EffectsGenerated(),
            EventListenersCollection::class.java to EventListenersGenerated(),
            ItemsCollection::class.java to ItemsGenerated(),
            LocationParser::class.java to LocationJsonParser(),
            LocationDescriptionsCollection::class.java to LocationDescriptionsGenerated(),
            RecipesCollection::class.java to RecipesGenerated(),
            ResourceHelper::class.java to KotlinResourceHelper(),
            SpellCommandsCollection::class.java to SpellCommandsGenerated(),
            StoryEventsCollection::class.java to StoryEventsGenerated(),
            WeathersCollection::class.java to WeathersGenerated(),
            WeatherStringsCollection::class.java to WeatherStringsGenerated(),
        )
    }


}
