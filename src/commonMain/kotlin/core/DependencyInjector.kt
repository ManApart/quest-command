package core

import conversation.dsl.DialoguesCollection
import conversation.dsl.DialoguesGenerated
import core.ai.action.dsl.AIActionsCollection
import core.ai.action.dsl.AIActionsGenerated
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsGenerated
import core.ai.knowledge.dsl.KnowledgeFindersCollection
import core.ai.knowledge.dsl.KnowledgeFindersGenerated
import core.body.BodyPartsCollection
import core.body.BodyPartsGenerated
import core.body.BodysCollection
import core.body.BodysGenerated
import core.commands.CommandsCollection
import core.commands.CommandsGenerated
import core.events.*
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.activator.dsl.ActivatorsGenerated
import core.thing.creature.CreaturesCollection
import core.thing.creature.CreaturesGenerated
import core.thing.item.ItemsCollection
import core.thing.item.ItemsGenerated
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
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsGenerated
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksGenerated
import traveling.location.weather.WeathersCollection
import traveling.location.weather.WeathersGenerated
import kotlin.reflect.KClass

object DependencyInjector {
    private val interfaces = mutableMapOf<KClass<*>, Any>()
    private val defaultImplementations by lazy { createDefaultImplementations() }


    fun <T : E, E : Any> setImplementation(thingInterface: KClass<E>, implementation: T) {
        interfaces[thingInterface] = implementation
    }

    fun <E : Any> clearImplementation(thingInterface: KClass<E>) {
        interfaces.remove(thingInterface)
    }

    fun clearAllImplementations() {
        interfaces.clear()
    }

    fun <T : E, E : Any> getImplementation(thingInterface: KClass<E>): T {
        return when {
            interfaces.containsKey(thingInterface) -> {
                @Suppress("UNCHECKED_CAST")
                interfaces[thingInterface] as T
            }
            defaultImplementations.containsKey(thingInterface) -> {
                @Suppress("UNCHECKED_CAST")
                defaultImplementations[thingInterface] as T
            }
            else -> throw IllegalArgumentException("No implementation could be found for interface ${thingInterface.simpleName}")
        }
    }

    private fun createDefaultImplementations(): Map<KClass<*>, Any> {
        return mapOf(
            ActivatorsCollection::class to ActivatorsGenerated(),
            AIActionsCollection::class to AIActionsGenerated(),
            BehaviorsCollection::class to BehaviorsGenerated(),
            BodysCollection::class to BodysGenerated(),
            BodyPartsCollection::class to BodyPartsGenerated(),
            CreaturesCollection::class to CreaturesGenerated(),
            ConditionsCollection::class to ConditionsGenerated(),
            CommandsCollection::class to CommandsGenerated(),
            DialoguesCollection::class to DialoguesGenerated(),
            EffectsCollection::class to EffectsGenerated(),
            EventListenerMapCollection::class to EventListenerMapGenerated(),
            ItemsCollection::class to ItemsGenerated(),
            KnowledgeFindersCollection::class to KnowledgeFindersGenerated(),
            LocationsCollection::class to LocationsGenerated(),
            NetworksCollection::class to NetworksGenerated(),
            RecipesCollection::class to RecipesGenerated(),
            ResourceHelper::class to KotlinResourceHelper(),
            SpellCommandsCollection::class to SpellCommandsGenerated(),
            StoryEventsCollection::class to StoryEventsGenerated(),
            WeathersCollection::class to WeathersGenerated(),
        )
    }


}
