package quests

class StoryEventsGenerated : StoryEventsCollection {
    override val values = listOf<StoryEventResource>(resources.storyEvents.ASimplePie(), resources.storyEvents.Tips(), resources.storyEvents.Tutorial()).flatMap { it.values }
}