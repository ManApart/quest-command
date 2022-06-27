package resources.storyEvents

import core.GameState
import core.eventWithPlayer
import quests.ConditionalEvents
import quests.StoryEvent
import quests.StoryEventResource
import status.LevelUpEvent
import status.stat.STAMINA
import status.statChanged.StatMinnedEvent
import system.message.MessageEvent

class Tips : StoryEventResource {
    override val values: List<StoryEvent> = listOf(
        StoryEvent("Tips", 1, "When I level up I should rest to restore my stats to their new levels.",
            ConditionalEvents(LevelUpEvent::class,
                { event, _ -> event.source.isPlayer() },
                { event, _ -> listOf(MessageEvent(GameState.getPlayer(event.source)!!, "When I level up I should rest to restore my stats to their new levels.")) }
            ), availableBefore = 1000, availableAfter = 0
        ),
        StoryEvent("Tips", 2, "I can rest to restore my stamina. I can rest by using the rest command and supplying how many hours to rest.",
            ConditionalEvents(StatMinnedEvent::class,
                { event, _ -> event.thing.isPlayer() && event.stat == STAMINA },
                { event, _ -> listOf(MessageEvent(GameState.getPlayer(event.thing)!!, "My stamina is empty. I should rest until it's recovered. I can rest by using the rest command and supplying how many hours to rest.")) }
            ), availableBefore = 1000, availableAfter = 0
        ),
    )
}