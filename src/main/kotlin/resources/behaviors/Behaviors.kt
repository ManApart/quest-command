package resources.behaviors

import core.GameState
import core.ai.behavior.BehaviorResource
import core.ai.behavior.behaviors
import core.commands.commandEvent.CommandEvent
import core.properties.propValChanged.PropertyStatMinnedEvent
import core.thing.activator.ActivatorManager
import core.utility.parseLocation
import crafting.DiscoverRecipeEvent
import crafting.RecipeManager
import inventory.pickupItem.ItemPickedUpEvent
import status.conditions.RemoveConditionEvent
import status.statChanged.StatChangeEvent
import system.message.MessageEvent
import system.message.messageEvent
import traveling.RestrictLocationEvent
import traveling.scope.remove.RemoveItemEvent
import traveling.scope.remove.RemoveScopeEvent
import traveling.scope.spawn.SpawnActivatorEvent
import traveling.scope.spawn.SpawnItemEvent
import use.UseEvent
import use.eat.EatFoodEvent
import use.interaction.InteractEvent

class CommonBehaviors : BehaviorResource {
    override val values = behaviors {
        behavior("Add on Eat", EatFoodEvent::class) {
            events { event, params ->
                listOf(SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, event.creature))
            }
        }

        behavior("Chop Tree", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "chopHealth"
            }
            events { event, params ->
                val treeName = params["treeName"] ?: "tree"
                listOfNotNull(
                    messageEvent(GameState.getPlayer(event.thing), "The $treeName cracks and falls to the ground."),
                    RemoveScopeEvent(event.thing),
                    SpawnActivatorEvent(ActivatorManager.getActivator("Logs"), thingLocation = event.thing.location),
                    SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, thingLocation = event.thing.location)
                )
            }
        }

        behavior("Climbable", InteractEvent::class) {
            events { event, _ ->
                listOfNotNull(CommandEvent(GameState.getPlayer(event.source),"climb ${event.thing.name}"))
            }
        }

        behavior("Burn to Ash", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "fireHealth"
            }
            events { event, params ->
                val name = params["name"] ?: "object"
                listOfNotNull(
                    messageEvent(GameState.getPlayer(event.thing), "The $name smolders until it is nothing more than ash."),
                    RemoveScopeEvent(event.thing),
                    SpawnItemEvent("Ash", params["count"]?.toInt() ?: 1, thingLocation = event.thing.location, positionParent = event.thing)
                )
            }
        }

        behavior("Burn Out", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "fireHealth" && event.thing.soul.hasEffect("On Fire")
            }
            events { event, params ->
                listOfNotNull(
                    messageEvent(GameState.getPlayer(event.thing), "The ${event.thing} smolders out and needs to be relit."),
                    RemoveConditionEvent(event.thing, event.thing.soul.getConditionWithEffect("On Fire")),
                    StatChangeEvent(event.thing, "lighting", "fireHealth", params["fireHealth"]?.toInt() ?: 1)
                )
            }
        }

        behavior("Slash Harvest", UseEvent::class) {
            condition { event, _ ->
                event.used.properties.tags.has("Sharp")
            }
            events { event, params ->
                listOfNotNull(
                    messageEvent(GameState.getPlayer(event.source), params["message"] ?: "You harvest ${event.usedOn} with ${event.used}."),
                    SpawnItemEvent(params["itemName"] ?: "Apple", params["count"]?.toInt() ?: 1, thingLocation = event.usedOn.location, positionParent = event.usedOn)
                )
            }
        }

        behavior("Restrict Destination", InteractEvent::class) {
            events { event, params ->
                val source = event.source
                val sourceLocation = parseLocation(params, source, "sourceNetwork", "sourceLocation")
                val destinationLocation = parseLocation(params, source, "destinationNetwork", "destinationLocation")
                val makeRestricted = false
                val replacement = ActivatorManager.getActivator(params["replacementActivator"] ?: "Logs")
                listOfNotNull(
                    messageEvent(GameState.getPlayer(source), params["message"] ?: ""),
                    RestrictLocationEvent(event.thing, sourceLocation, destinationLocation, makeRestricted),
                    RemoveScopeEvent(event.thing),
                    SpawnActivatorEvent(replacement, true, event.thing.location)
                )
            }
        }

        behavior("Mill", ItemPickedUpEvent::class) {
            condition { event, params ->
                event.item.name == params["sourceItem"]
            }
            events { event, params ->
                val resultItem = params["resultItem"] ?: "Wheat Flour"
                val sourceItemName = params["sourceItem"] ?: "Wheat Bundle"
                val sourceItem = event.source.inventory.getItem(sourceItemName)
                val depositLocation = parseLocation(params, event.source, "resultItemNetwork", "resultItemLocation")
                val depositThing = depositLocation.getLocation().getThings(params["resultContainer"] ?: "Grain Bin").firstOrNull()
                if (sourceItem == null || depositThing == null) {
                    listOfNotNull(messageEvent(GameState.getPlayer(event.source), "Unable to Mill."))
                } else {
                    listOfNotNull(
                        messageEvent(GameState.getPlayer(event.source), "The ${event.item.name} slides down the chute and is milled into $resultItem as it collects in the ${depositThing.name}."),
                        RemoveItemEvent(event.source, sourceItem),
                        SpawnItemEvent(resultItem, 1, depositThing)
                    )
                }
            }
        }

        behavior("Learn Recipe", InteractEvent::class) {
            events { event, params ->
                val sourceItem = event.source.inventory.getItem(params["sourceItem"])
                val recipe = RecipeManager.getRecipeOrNull(params["recipe"] ?: "")
                when {
                    recipe == null -> listOf()
                    !event.source.isPlayer() -> listOf()
                    sourceItem == null -> listOfNotNull(DiscoverRecipeEvent(GameState.getPlayer(event.source), recipe))
                    else -> listOfNotNull(
                        RemoveItemEvent(event.source, sourceItem),
                        RemoveScopeEvent(sourceItem),
                        DiscoverRecipeEvent(GameState.getPlayer(event.source), recipe)
                    )
                }
            }
        }
    }
}