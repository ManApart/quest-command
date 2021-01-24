package resources.behaviors

import core.GameState
import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorResource
import core.commands.commandEvent.CommandEvent
import core.properties.propValChanged.PropertyStatMinnedEvent
import core.target.activator.ActivatorManager
import core.utility.parseLocation
import crafting.DiscoverRecipeEvent
import crafting.RecipeManager
import inventory.pickupItem.ItemPickedUpEvent
import quests.ConditionalEvents
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

class BaseBehaviors : BehaviorResource {
    override val values = listOf<Behavior<*>>(
            Behavior("Add on Eat", ConditionalEvents(EatFoodEvent::class.java, createEvents = { _, params ->
                listOf(SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1, GameState.player))
            })),

            Behavior("Chop Tree", ConditionalEvents(PropertyStatMinnedEvent::class.java, { event, _ ->
                event.stat == "chopHealth"
            }, { event, params ->
                val treeName = params["treeName"] ?: "tree"
                listOf(
                        MessageEvent("The $treeName cracks and falls to the ground."),
                        RemoveScopeEvent(event.target),
                        SpawnActivatorEvent(ActivatorManager.getActivator("Logs")),
                        SpawnItemEvent(params["resultItemName"] ?: "Apple", params["count"]?.toInt() ?: 1)
                )
            })),

            Behavior("Climbable", ConditionalEvents(InteractEvent::class.java, createEvents = { event, _ ->
                listOf(CommandEvent("climb ${event.target.name}"))
            })),

            Behavior("Burn to Ash", ConditionalEvents(PropertyStatMinnedEvent::class.java, { event, _ ->
                event.stat == "fireHealth"
            }, { event, params ->
                val name = params["name"] ?: "object"
                listOf(
                        MessageEvent("The $name smolders until it is nothing more than ash."),
                        RemoveScopeEvent(event.target),
                        SpawnItemEvent("Ash", params["count"]?.toInt() ?: 1, positionParent = event.target)
                )
            })),

            Behavior("Burn Out", ConditionalEvents(PropertyStatMinnedEvent::class.java, { event, _ ->
                event.stat == "fireHealth" && event.target.soul.hasEffect("On Fire")
            }, { event, params ->
                listOf(
                        MessageEvent("The ${event.target} smolders out and needs to be relit."),
                        RemoveConditionEvent(event.target, event.target.soul.getConditionWithEffect("On Fire")),
                        StatChangeEvent(event.target, "lighting", "fireHealth", params["fireHealth"]?.toInt() ?: 1)
                )
            })),

            Behavior("Slash Harvest", ConditionalEvents(UseEvent::class.java, { event, _ ->
                event.used.properties.tags.has("Sharp")
            }, { event, params ->
                listOf(
                        MessageEvent(params["message"] ?: "You harvest ${event.target} with ${event.used}."),
                        SpawnItemEvent(params["itemName"] ?: "Apple", params["count"]?.toInt() ?: 1, positionParent = event.target)
                )
            })),

            Behavior("Restrict Destination", ConditionalEvents(InteractEvent::class.java, createEvents = { event, params ->
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
            })),

            Behavior("Mill", ConditionalEvents(ItemPickedUpEvent::class.java, { event, params ->
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
            })),

            Behavior("Learn Recipe", ConditionalEvents(InteractEvent::class.java, createEvents = { event, params ->
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
            })),
    )
}