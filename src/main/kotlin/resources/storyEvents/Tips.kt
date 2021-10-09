package resources.storyEvents

import quests.ConditionalEvents
import quests.StoryEvent
import quests.StoryEventResource
import status.LevelUpEvent
import system.message.MessageEvent

class Tips : StoryEventResource {
    override val values: List<StoryEvent> = listOf(
            StoryEvent("Tips", 1, "When I level up I should rest to restore my stats to their new levels.",
                    ConditionalEvents(LevelUpEvent::class,
                            { event, _ -> event.source.isPlayer()},
                            { event, _ -> listOf(MessageEvent(event.source,"When I level up I should rest to restore my stats to their new levels.")) }
                    ), availableBefore = 1000, availableAfter = 0
            ),


    )

}