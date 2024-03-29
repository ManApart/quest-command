package resources.storyEvents

import core.GameState
import core.eventWithPlayer
import explore.look.LookEvent
import explore.map.ReadMapEvent
import quests.*
import quests.journal.ViewQuestListEvent
import system.help.ViewHelpEvent
import system.message.MessageEvent
import system.startup.GameStartEvent
import traveling.arrive.ArriveEvent
import traveling.move.MoveEvent
import use.interaction.InteractEvent

class Tutorial : StoryEventResource {
    override val values: List<StoryEvent> = listOf(
        StoryEvent("Tutorial", 10, "I can remember what I can do by typing 'help commands'. I should do that now.",
            ConditionalEvents(GameStartEvent::class,
                createEvents = { _, _ -> listOfNotNull(MessageEvent(GameState.player, "To see what I can do I should type 'help commands'")) }
            )
        ),

        StoryEvent("Tutorial", 20, "To see my progress I should type `journal`.",
            ConditionalEvents(ViewHelpEvent::class,
                createEvents = { event, _ -> listOf(MessageEvent(event.source, "I have made progress in a quest! To see my progress I should type `journal`.")) }
            )
        ),

        StoryEvent("Tutorial", 30, "I should read my map.",
            ConditionalEvents(ViewQuestListEvent::class,
                createEvents = { event, _ ->
                    listOfNotNull(eventWithPlayer(event.source) {
                        MessageEvent(
                            it,
                            "I should familiarize myself with some basic commands. I'll start by typing 'map' to see where I am."
                        )
                    })
                }
            )
        ),

        StoryEvent("Tutorial", 40, "I should travel to Farmer's Hut.",
            ConditionalEvents(
                ReadMapEvent::class,
                createEvents = { event, _ -> listOf(MessageEvent(event.source, "I should travel to the Farmer's Hut. I can do so by typing 'travel farmer's hut'.")) }
            )
        ),

        StoryEvent("Tutorial", 50, "I should continue traveling to the interior of Farmer's Hut.",
            ConditionalEvents(ArriveEvent::class,
                { event, _ -> event.creature.isPlayer() && event.destination.location.name == "Farmer's Hut" },
                { event, _ -> listOfNotNull(eventWithPlayer(event.creature) { MessageEvent(it, "Now I should travel to the Farmer's Hut Interior.") }) }
            )
        ),

        StoryEvent("Tutorial", 60, "I should look for the Apple Pie Recipe.",
            ConditionalEvents(ArriveEvent::class,
                { event, _ -> event.creature.isPlayer() && event.destination.location.name == "Farmer's Hut Interior" },
                { event, _ -> listOfNotNull(eventWithPlayer(event.creature) { MessageEvent(it, "I should use the 'look' command to see what surrounds me.") }) }
            )
        ),

        StoryEvent("Tutorial", 70, "I should move to that recipe for Apple Pie by typing 'move 0,10,0'.",
            ConditionalEvents(LookEvent::class,
                { event, _ -> event.source.location.name == "Farmer's Hut Interior" },
                { event, _ -> listOf(MessageEvent(event.source, "I should move to that recipe for Apple Pie by typing 'move 0,10,0'.")) }
            )
        ),

        StoryEvent("Tutorial", 80, "I should read the recipe for Apple Pie by typing 'read recipe'.",
            ConditionalEvents(MoveEvent::class,
                { event, _ ->
                    if (event.creature.location.name == "Farmer's Hut Interior") {
                        val recipe = event.creature.location.getLocation().getItems("Apple Pie Recipe").firstOrNull()
                        (recipe != null && event.creature.canReach(recipe.position))
                    } else {
                        false
                    }
                },
                { event, _ -> listOfNotNull(eventWithPlayer(event.creature) { MessageEvent(it, "I should read the recipe for Apple Pie by typing 'read recipe'.") }) }
            )
        ),

        StoryEvent("Tutorial", 90, "I should travel to Barren Field.",
            ConditionalEvents(InteractEvent::class,
                { event, _ -> event.creature.isPlayer() && event.interactionTarget.name == "Apple Pie Recipe" },
                { event, _ -> listOfNotNull(eventWithPlayer(event.creature) { MessageEvent(it, "Once I'm done here I should travel to Barren Field.") }) }
            )
        ),

        StoryEvent("Tutorial", 100, "I now have a basic knowledge of how I can explore this world. I can always learn more by using 'help commands' or 'help look' (or another command).",
            ConditionalEvents(QuestStageUpdatedEvent::class,
                { event, _ -> event.quest == QuestManager.quests.get("Tutorial") && event.stage == 90 },
                { event, _ -> listOfNotNull(eventWithPlayer(event.source) { MessageEvent(it, "Now that I've completed these tasks I feel ready to explore the world.") }) }
            ), completesQuest = true
        ),


        )

}