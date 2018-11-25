package core.gameState.quests

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

class SetQuestStage : EventListener<SetQuestStageEvent>() {

    override fun execute(event: SetQuestStageEvent) {
        event.quest.executeStage(event.stage)
    }


}