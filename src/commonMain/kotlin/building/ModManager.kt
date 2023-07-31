package building

import conversation.dsl.DialogueTree
import core.ai.agenda.Agenda
import core.ai.behavior.Behavior
import core.ai.desire.DesireTree
import core.events.EventListener
import core.thing.ThingBuilder
import crafting.RecipeBuilder
import crafting.material.Material
import quests.StoryEvent
import status.conditions.ConditionRecipe
import status.effects.EffectBase
import traveling.location.location.LocationRecipeBuilder
import traveling.location.network.NetworkBuilder
import traveling.location.weather.Weather

object ModManager {
    val eventListeners = mutableMapOf<String, MutableList<EventListener<*>>>()
    val activators = mutableListOf<ThingBuilder>()
    val ai = mutableListOf<DesireTree>()
    val agendas = mutableListOf<Agenda>()
    val behaviors = mutableListOf<Behavior<*>>()
    val bodies = mutableListOf<NetworkBuilder>()
    val bodyParts = mutableListOf<LocationRecipeBuilder>()
    val creatures = mutableListOf<ThingBuilder>()
    val conditions = mutableListOf<ConditionRecipe>()
    val conversations = mutableListOf<DialogueTree>()
    val effects = mutableListOf<EffectBase>()
    val items = mutableListOf<ThingBuilder>()
    val networks = mutableListOf<NetworkBuilder>()
    val locations = mutableListOf<LocationRecipeBuilder>()
    val materials = mutableListOf<Material>()
    val quests = mutableListOf<StoryEvent>()
    val recipes = mutableListOf<RecipeBuilder>()
    val weather = mutableListOf<Weather>()

    fun reset(){
        activators.clear()
        ai.clear()
        agendas.clear()
        behaviors.clear()
        bodies.clear()
        bodyParts.clear()
        creatures.clear()
        conditions.clear()
        conversations.clear()
        effects.clear()
        items.clear()
        networks.clear()
        locations.clear()
        materials.clear()
        quests.clear()
        recipes.clear()
        weather.clear()
    }
}