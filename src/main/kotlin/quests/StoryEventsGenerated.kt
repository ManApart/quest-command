package quests
import quests.StoryEvent

class StoryEventsGenerated : StoryEventsCollection {
    override val values: List<StoryEvent> = listOf(resources.storyEvents.ASimplePie(), resources.storyEvents.Tips(), resources.storyEvents.Tutorial()).flatMap { it.values }
}