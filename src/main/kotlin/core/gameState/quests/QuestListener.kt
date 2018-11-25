package core.gameState.quests

import core.events.Event
import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.history.display
import crafting.CookAttemptEvent
import crafting.Recipe
import crafting.RecipeManager
import inventory.pickupItem.PickupItemEvent
import system.EventManager
import system.ItemManager

//TODO - instead of cycling all quests every event, maybe create a list of listened for events and only trigger on those
class QuestListener : EventListener<Event>() {

    override fun execute(event: Event) {
        QuestManager.quests.forEach { quest->
            if (quest.matches(event)){
                quest.execute()
            }
        }
    }
}