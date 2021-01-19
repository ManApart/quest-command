package resources.storyEvents

import core.GameState
import quests.StoryEvent2
import quests.StoryEventResource
import quests.TriggeredEvent2
import system.message.MessageEvent
import use.interaction.InteractEvent

class ASimplePie : StoryEventResource {
    override val values: List<StoryEvent2> = listOf(
            StoryEvent2("A Simple Pie", "Read Recipe", 10, "I should pick up an Apple, which I could get by traveling to the Apple Tree.",
                    TriggeredEvent2(InteractEvent::class.java,
                            { event, _ -> event.source == GameState.player && event.target.name == "Apple Pie Recipe" },
                            { _, _ -> listOf(MessageEvent("This pie sounds pretty simple to make. I should start by getting an Apple.")) }
                    )
            )
    )

}