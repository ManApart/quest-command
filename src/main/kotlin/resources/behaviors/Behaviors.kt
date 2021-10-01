package resources.behaviors

import core.ai.behavior.BehaviorResource
import core.ai.behavior.behaviors
import core.commands.commandEvent.CommandEvent
import core.properties.propValChanged.PropertyStatMinnedEvent
import core.target.activator.ActivatorManager
import core.utility.parseLocation
import crafting.DiscoverRecipeEvent
import crafting.RecipeManager
import inventory.pickupItem.ItemPickedUpEvent
import status.conditions.RemoveConditionEvent
import status.statChanged.StatChangeEvent
import system.message.MessageEvent
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
                listOf(SpawnItemEvent( params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, event.creature))
            }
        }

        behavior("Chop Tree", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "chopHealth"
            }
            events { event, params ->
                val treeName = params["treeName"] ?: "tree"
                listOf(
                    MessageEvent("The $treeName cracks and falls to the ground."),
                    RemoveScopeEvent(event.target),
                    SpawnActivatorEvent(ActivatorManager.getActivator("Logs"), targetLocation = event.target.location),
                    SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, targetLocation = event.target.location)
                )
            }
        }

        behavior("Climbable", InteractEvent::class) {
            events { event, _ ->
                listOf(CommandEvent("climb ${event.target.name}"))
            }
        }

        behavior("Burn to Ash", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "fireHealth"
            }
            events { event, params ->
                val name = params["name"] ?: "object"
                listOf(
                    MessageEvent("The $name smolders until it is nothing more than ash."),
                    RemoveScopeEvent(event.target),
                    SpawnItemEvent("Ash", params["count"]?.toInt() ?: 1, targetLocation = event.target.location, positionParent = event.target)
                )
            }
        }

        behavior("Burn Out", PropertyStatMinnedEvent::class) {
            condition { event, _ ->
                event.stat == "fireHealth" && event.target.soul.hasEffect("On Fire")
            }
            events { event, params ->
                listOf(
                    MessageEvent("The ${event.target} smolders out and needs to be relit."),
                    RemoveConditionEvent(event.target, event.target.soul.getConditionWithEffect("On Fire")),
                    StatChangeEvent(event.target, "lighting", "fireHealth", params["fireHealth"]?.toInt() ?: 1)
                )
            }
        }

        behavior("Slash Harvest", UseEvent::class) {
            condition { event, _ ->
                event.used.properties.tags.has("Sharp")
            }
            events { event, params ->
                listOf(
                    MessageEvent(params["message"] ?: "You harvest ${event.target} with ${event.used}."),
                    SpawnItemEvent(params["itemName"] ?: "Apple", params["count"]?.toInt() ?: 1, targetLocation = event.target.location, positionParent = event.target)
                )
            }
        }

        behavior("Restrict Destination", InteractEvent::class) {
            events { event, params ->
                val sourceLocation = parseLocation(params, event.source, "sourceNetwork", "sourceLocation")
                val destinationLocation = parseLocation(params, event.source, "destinationNetwork", "destinationLocation")
                val makeRestricted = false
                val replacement = ActivatorManager.getActivator(params["replacementActivator"] ?: "Logs")
                listOf(
                    MessageEvent(params["message"] ?: ""),
                    RestrictLocationEvent(sourceLocation, destinationLocation, makeRestricted),
                    RemoveScopeEvent(event.target),
                    SpawnActivatorEvent(replacement, true, event.target.location)
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
                val depositTarget = depositLocation.getLocation().getTargets(params["resultContainer"] ?: "Grain Bin").firstOrNull()
                if (sourceItem == null || depositTarget == null) {
                    listOf(MessageEvent("Unable to Mill."))
                } else {
                    listOf(
                        MessageEvent("The ${event.item.name} slides down the chute and is milled into $resultItem as it collects in the $depositTarget."),
                        RemoveItemEvent(event.source, sourceItem),
                        SpawnItemEvent(resultItem, 1, depositTarget)
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
                    sourceItem == null -> listOf(DiscoverRecipeEvent(event.source, recipe))
                    else -> listOf(
                        RemoveItemEvent(event.source, sourceItem),
                        RemoveScopeEvent(sourceItem),
                        DiscoverRecipeEvent(event.source, recipe)
                    )
                }
            }
        }
    }
}