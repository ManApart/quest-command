package resources.behaviors

import core.GameState
import core.ai.behavior.Behavior2
import core.commands.commandEvent.CommandEvent
import core.properties.propValChanged.PropertyStatMinnedEvent
import core.target.Target
import core.target.activator.ActivatorManager
import crafting.DiscoverRecipeEvent
import crafting.RecipeManager
import inventory.pickupItem.ItemPickedUpEvent
import status.conditions.RemoveConditionEvent
import status.statChanged.StatChangeEvent
import system.message.MessageEvent
import traveling.RestrictLocationEvent
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import traveling.scope.remove.RemoveItemEvent
import traveling.scope.remove.RemoveScopeEvent
import traveling.scope.spawn.SpawnActivatorEvent
import traveling.scope.spawn.SpawnItemEvent
import use.UseEvent
import use.eat.EatFoodEvent
import use.interaction.InteractEvent

val behaviorsList = listOf<Behavior2<*>>(
        Behavior2("Add on Eat", EatFoodEvent::class.java, createEvents = { event, params ->
            listOf(SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, GameState.player))
        }),
        Behavior2("Chop Tree", PropertyStatMinnedEvent::class.java, { event, params ->
            event.stat == "chopHealth"
        }, { event, params ->
            val treeName = params["treeName"] ?: "tree"
            listOf(
                    MessageEvent("The $treeName cracks and falls to the ground."),
                    RemoveScopeEvent(event.target),
                    SpawnActivatorEvent(ActivatorManager.getActivator("Logs")),
                    SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1)
            )
        }),
        Behavior2("Climbable", InteractEvent::class.java, createEvents = { event, params ->
            listOf(CommandEvent("climb ${event.target.name}"))
        }),
        Behavior2("Burn to Ash", PropertyStatMinnedEvent::class.java, { event, params ->
            event.stat == "fireHealth"
        }, { event, params ->
            val name = params["name"] ?: "object"
            listOf(
                    MessageEvent("The $name smolders until it is nothing more than ash."),
                    RemoveScopeEvent(event.target),
                    SpawnItemEvent("Ash", params["count"]?.toInt() ?: 1, positionParent = event.target)
            )
        }),
        Behavior2("Burn Out", PropertyStatMinnedEvent::class.java, { event, params ->
            event.stat == "fireHealth" && event.target.soul.hasEffect("On Fire")
        }, { event, params ->
            listOf(
                    MessageEvent("The ${event.target} smolders out and needs to be relit."),
                    RemoveConditionEvent(event.target, event.target.soul.getConditionWithEffect("On Fire")),
                    StatChangeEvent(event.target, "lighting", "fireHealth", params["fireHealth"]?.toInt() ?: 1)
            )
        }),
        Behavior2("Slash Harvest", UseEvent::class.java, { event, params ->
            event.used.properties.tags.has("Sharp")
        }, { event, params ->
            listOf(
                    MessageEvent(params["message"] ?: "You harvest ${event.target} with ${event.used}."),
                    RemoveConditionEvent(event.target, event.target.soul.getConditionWithEffect("On Fire")),
                    SpawnItemEvent(params["itemName"] ?: "Apple", params["count"]?.toInt() ?: 1, positionParent = event.target)
            )
        }),
        Behavior2("Restrict Destination", InteractEvent::class.java, createEvents = { event, params ->
            val sourceLocation = parseLocation(params, event.source, "sourceNetwork", "sourceLocation")
            val destinationLocation = parseLocation(params, event.source, "destinationNetwork", "destinationLocation")
            val makeRestricted = false
            val replacement = ActivatorManager.getActivator(params["replacementActivator"] ?: "Logs")
            listOf(
                    MessageEvent(params["message"] ?: ""),
                    RestrictLocationEvent(sourceLocation, destinationLocation, makeRestricted),
                    RemoveScopeEvent(event.target),
                    SpawnActivatorEvent(replacement, true)
            )
        }),
        Behavior2("Mill", ItemPickedUpEvent::class.java, { event, params ->
            event.item.name == params["sourceItem"]
        }, { event, params ->
            val resultItem = params["resultItem"] ?: "Wheat Flour"
            val sourceItemName = params["sourceItem"] ?: "Wheat Bundle"
            val sourceItem = event.source.inventory.getItem(sourceItemName)
            val depositLocation = parseLocation(params, event.source, "resultItemNetwork", "resultItemLocation")
            val depositTarget = depositLocation.getLocation().getTargets(params["resultContainer"] ?: "Grain Bin").firstOrNull()
            if (sourceItem == null || depositTarget == null) {
                listOf(MessageEvent("Unable to Mill."))
            } else {
                listOf(
                        MessageEvent("The ${event.item} slides down the chute and is milled into $resultItem as it collects in the $depositTarget."),
                        RemoveItemEvent(event.source, sourceItem),
                        SpawnItemEvent(resultItem, 1, depositTarget)
                )
            }
        }),
        Behavior2("Learn Recipe", InteractEvent::class.java, createEvents = { event, params ->
            val sourceItemName = params["sourceItem"] ?: "Wheat Bundle"
            val sourceItem = event.source.inventory.getItem(sourceItemName)
            val recipe = RecipeManager.getRecipeOrNull(params["recipe"] ?: "")

            if (sourceItem == null || recipe == null) {
                listOf()
            } else {
                listOf(
                        RemoveItemEvent(event.source, sourceItem),
                        RemoveScopeEvent(sourceItem),
                        DiscoverRecipeEvent(event.source, recipe)
                )
            }
        }),
)


//TODO - move all helper functions somewhere else
private fun parseLocation(params: Map<String, String>, parent: Target, networkNameKey: String, locationNameKey: String): LocationNode {
    val network = if (params.containsKey(networkNameKey)) {
        LocationManager.getNetwork(params[networkNameKey]!!)
    } else {
        parent.location.network
    }
    if (params.containsKey(locationNameKey)) {
        if (network.locationNodeExists(params[locationNameKey]!!)) {
            return network.getLocationNode(params[locationNameKey]!!)
        }
    }
    return parent.location
}