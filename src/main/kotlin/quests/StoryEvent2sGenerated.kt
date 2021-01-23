package quests

class StoryEvent2sGenerated : StoryEvent2sCollection {
    override val values: List<StoryEvent2> = listOf(resources.storyEvents.ASimplePie(), resources.storyEvents.Tips(), resources.storyEvents.Tutorial()).flatMap { it.values }
}