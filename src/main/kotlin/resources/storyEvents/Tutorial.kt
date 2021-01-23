package resources.storyEvents

import core.GameState
import explore.look.LookEvent
import explore.map.ReadMapEvent
import inventory.pickupItem.ItemPickedUpEvent
import inventory.pickupItem.TakeItemEvent
import inventory.putItem.TransferItemEvent
import quests.*
import quests.journal.ViewQuestListEvent
import system.help.ViewHelpEvent
import system.message.MessageEvent
import system.startup.GameStartEvent
import traveling.arrive.ArriveEvent
import use.interaction.InteractEvent

class Tutorial : StoryEventResource {
    override val values: List<StoryEvent2> = listOf(
            StoryEvent2("Tutorial", 10, "I can remember what I can do by typing 'help commands'. I should do that now.",
                    TriggeredEvent2(GameStartEvent::class.java,
                            createEvents = { _, _ -> listOf(MessageEvent("To see what I can do I should type 'help commands'")) }
                    )
            ),

            StoryEvent2("Tutorial", 20, "To see my progress I should type `journal`.",
                    TriggeredEvent2(ViewHelpEvent::class.java,
                            createEvents = { _, _ -> listOf(MessageEvent("I have made progress in a quest! To see my progress I should type `journal`.")) }
                    )
            ),

            StoryEvent2("Tutorial", 30, "I should read my map.",
                    TriggeredEvent2(ViewQuestListEvent::class.java,
                            createEvents = { _, _ -> listOf(MessageEvent("I should familiarize myself with some basic commands. I'll start by typing 'map' to see where I am.")) }
                    )
            ),

            StoryEvent2("Tutorial", 40, "I should travel to Farmer's Hut.",
                    TriggeredEvent2(ReadMapEvent::class.java,
                            createEvents = { _, _ -> listOf(MessageEvent("I should travel to the Farmer's Hut. I can do so by typing 'travel farmer's hut'.")) }
                    )
            ),

            StoryEvent2("Tutorial", 50, "I should continue traveling to the interior of Farmer's Hut.",
                    TriggeredEvent2(ArriveEvent::class.java,
                            { event, _ -> event.creature == GameState.player && event.destination.location.name == "Farmer's Hut" },
                            { _, _ -> listOf(MessageEvent("Now I should travel to the Farmer's Hut Interior.")) }
                    )
            ),

            StoryEvent2("Tutorial", 60, "I should look for the Apple Pie Recipe.",
                    TriggeredEvent2(ArriveEvent::class.java,
                            { event, _ -> event.creature == GameState.player && event.destination.location.name == "Farmer's Hut Interior" },
                            { _, _ -> listOf(MessageEvent("I should use the 'look' command to see what surrounds me.")) }
                    )
            ),

            StoryEvent2("Tutorial", 70, "I should move to and then read the recipe for Apple Pie.",
                    TriggeredEvent2(LookEvent::class.java,
                            { event, _ -> event.source.location.name == "Farmer's Hut Interior" },
                            { _, _ -> listOf(MessageEvent("I should move to and then read the recipe for Apple Pie.")) }
                    )
            ),

            //TODO - add how to move to recipe

            StoryEvent2("Tutorial", 80, "I should travel to Barren Field.",
                    TriggeredEvent2(InteractEvent::class.java,
                            { event, _ -> event.source == GameState.player && event.target.name == "Apple Pie Recipe" },
                            { _, _ -> listOf(MessageEvent("Once I'm done here I should travel to Barren Field.")) }
                    )
            ),

            StoryEvent2("Tutorial", 90, "I now have a basic knowledge of how I can explore this world.",
                    TriggeredEvent2(QuestStageUpdatedEvent::class.java,
                            { event, _ -> event.quest == QuestManager.quests.get("Tutorial") && event.stage == 80 },
                            { _, _ -> listOf(MessageEvent("Now that I've completed these tasks I feel ready to explore the world.")) }
                    ), completesQuest = true
            ),


            )

}