package resources.storyEvents

import core.GameState
import inventory.pickupItem.ItemPickedUpEvent
import inventory.pickupItem.TakeItemEvent
import inventory.putItem.TransferItemEvent
import quests.CompleteQuestEvent
import quests.StoryEvent2
import quests.StoryEventResource
import quests.TriggeredEvent2
import status.LevelUpEvent
import system.message.MessageEvent
import traveling.arrive.ArriveEvent
import use.interaction.InteractEvent

class Tips : StoryEventResource {
    override val values: List<StoryEvent2> = listOf(
            StoryEvent2("Tips", 1, "When I level up I should rest to restore my stats to their new levels.",
                    TriggeredEvent2(LevelUpEvent::class.java,
                            { event, _ -> event.source == GameState.player},
                            { _, _ -> listOf(MessageEvent("When I level up I should rest to restore my stats to their new levels.")) }
                    ), availableBefore = 1000, availableAfter = 0
            ),


    )

}