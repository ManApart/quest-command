import conversation.dsl.DialoguesCollection
import conversation.dsl.DialoguesMock
import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.AIManager
import core.ai.action.dsl.AIActionsCollection
import core.ai.action.dsl.AIActionsMock
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.ai.dsl.AIsCollection
import core.ai.dsl.AIsMock
import core.body.BodyManager
import core.commands.CommandParser
import core.commands.CommandsCollection
import core.commands.CommandsMock
import core.events.EventListenersCollection
import core.events.EventListenersMock
import core.events.EventManager
import core.properties.*
import core.target.Target
import core.target.activator.ACTIVATOR_TAG
import core.target.activator.ActivatorManager
import core.target.activator.ActivatorParser
import core.target.creature.CREATURE_TAG
import core.target.creature.CreatureParser
import core.target.item.ITEM_TAG
import core.target.item.ItemManager
import core.target.item.ItemParser
import crafting.RecipeFakeParser
import crafting.RecipeManager
import crafting.RecipeParser
import inventory.createInventoryBody
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import quests.QuestManager
import quests.StoryEventsCollection
import quests.StoryEventsMock
import status.conditions.ConditionFakeParser
import status.conditions.ConditionManager
import status.conditions.ConditionParser
import status.effects.EffectFakeParser
import status.effects.EffectManager
import status.effects.EffectParser
import status.stat.STRENGTH
import system.ActivatorFakeParser
import system.BodyFakeParser
import system.ItemFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.weather.WeatherFakeParser
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeatherParser

fun createItem(name: String = "Apple", weight: Int = 1): Target {
    return Target(
        name, properties = Properties(
            Values(mapOf("weight" to weight.toString())),
            Tags(listOf(ITEM_TAG))
        )
    )
}

//Pouch is a container that is also an item
fun createPouch(size: Int = 5, weight: Int = 1): Target {
    return Target(
        "Pouch",
        body = createInventoryBody("Pouch", size),
        properties = Properties(
            Values(
                mapOf(
                    SIZE to size.toString(),
                    WEIGHT to weight.toString()
                )
            ),
            Tags(listOf(ITEM_TAG, CONTAINER, OPEN, ITEM_TAG))
        )
    )
}

// Chest is a container
fun createChest(size: Int = 10): Target {
    return Target(
        "Chest", body = createInventoryBody("Chest", size),
        properties = Properties(
            Values(mapOf(SIZE to size.toString())),
            Tags(listOf(CONTAINER, OPEN, ACTIVATOR_TAG))
        )
    )
}

fun createClosedChest(size: Int = 10): Target {
    return Target(
        "Closed Chest",
        body = createInventoryBody("Closed Chest", size),
        properties = Properties(
            Values(mapOf(SIZE to size.toString())),
            Tags(listOf(CONTAINER, ACTIVATOR_TAG))
        )
    )
}

fun createPackMule(strength: Int = 1): Target {
    return Target(
        "Pack Mule", body = createInventoryBody("Pack Mule"),
        properties = Properties(
            Values(mapOf(STRENGTH to strength.toString())),
            Tags(listOf(CONTAINER, OPEN, CREATURE_TAG))
        )
    )
}

fun createMockedGame() {
    DependencyInjector.clearAllImplementations()
    DependencyInjector.setImplementation(ActivatorParser::class.java, ActivatorFakeParser())
    ActivatorManager.reset()

    DependencyInjector.setImplementation(AIActionsCollection::class.java, AIActionsMock())
    DependencyInjector.setImplementation(AIsCollection::class.java, AIsMock())
    AIManager.reset()

    DependencyInjector.setImplementation(BehaviorsCollection::class.java, BehaviorsMock())
    BehaviorManager.reset()

    DependencyInjector.setImplementation(LocationParser::class.java, BodyFakeParser.parserWithFakePlayer())
    BodyManager.reset()

//    DependencyInjector.setImplementation(CreatureParser::class.java, Creature())

    DependencyInjector.setImplementation(CommandsCollection::class.java, CommandsMock())

    DependencyInjector.setImplementation(ConditionParser::class.java, ConditionFakeParser())
    ConditionManager.reset()

    DependencyInjector.setImplementation(DialoguesCollection::class.java, DialoguesMock())

    DependencyInjector.setImplementation(EffectParser::class.java, EffectFakeParser())
    EffectManager.reset()

    DependencyInjector.setImplementation(EventListenersCollection::class.java, EventListenersMock())
    EventManager.reset()

    DependencyInjector.setImplementation(ItemParser::class.java, ItemFakeParser())
    ItemManager.reset()

    DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())
    LocationManager.reset()

    DependencyInjector.setImplementation(RecipeParser::class.java, RecipeFakeParser())
    RecipeManager.reset()

    DependencyInjector.setImplementation(StoryEventsCollection::class.java, StoryEventsMock())
    QuestManager.reset()

    DependencyInjector.setImplementation(SpellCommandsCollection::class.java, SpellCommandsMock())

//    DependencyInjector.setImplementation(Reflections::class.java, MockReflections())

//    DependencyInjector.setImplementation(ResourceHelper::class.java, FakeR())

    DependencyInjector.setImplementation(WeatherParser::class.java, WeatherFakeParser())
    WeatherManager.reset()

    EventManager.clear()
    CommandParser.setResponseRequest(null)
    GameState.player = GameManager.newPlayer()

}

