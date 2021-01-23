package resources.storyEvents

import core.GameState
import quests.StoryEvent
import quests.StoryEventResource
import quests.ConditionalEvents
import status.LevelUpEvent
import system.message.MessageEvent

class Tips : StoryEventResource {
    override val values: List<StoryEvent> = listOf(
            StoryEvent("Tips", 1, "When I level up I should rest to restore my stats to their new levels.",
                    ConditionalEvents(LevelUpEvent::class.java,
                            { event, _ -> event.source == GameState.player},
                            { _, _ -> listOf(MessageEvent("When I level up I should rest to restore my stats to their new levels.")) }
                    ), availableBefore = 1000, availableAfter = 0
            ),


    )

}