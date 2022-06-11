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
import core.body.*
import core.commands.CommandParsers
import core.commands.CommandsCollection
import core.commands.CommandsMock
import core.events.EventListenerMapGenerated
import core.events.EventListenerMapMock
import core.events.EventManager
import core.history.GameLogger
import core.properties.*
import core.thing.Thing
import core.thing.ThingBuilder
import core.thing.activator.ACTIVATOR_TAG
import core.thing.activator.ActivatorManager
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.activator.dsl.ActivatorsMock
import core.thing.creature.CREATURE_TAG
import core.thing.item.ITEM_TAG
import core.thing.item.ItemManager
import core.thing.item.ItemsCollection
import core.thing.item.ItemsMock
import core.thing.thing
import crafting.RecipeManager
import crafting.RecipesCollection
import crafting.RecipesMock
import inventory.createInventoryBody
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import quests.QuestManager
import quests.StoryEventsCollection
import quests.StoryEventsMock
import status.conditions.ConditionManager
import status.conditions.ConditionsCollection
import status.conditions.ConditionsGenerated
import status.effects.EffectManager
import status.effects.EffectsCollection
import status.effects.EffectsMock
import status.stat.STRENGTH
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeathersCollection
import traveling.location.weather.WeathersMock

fun createItem(name: String = "Apple", weight: Int = 1): Thing {
    return createItemBuilder(name, weight).build()
}

fun createItemBuilder(name: String = "Apple", weight: Int = 1): ThingBuilder {
    return thing(name) {
        props {
            value("weight" to weight)
            tag(ITEM_TAG)
        }
    }
}

//Pouch is a container that is also an item
fun createPouch(size: Int = 5, weight: Int = 1): Thing {
    return Thing(
        "Pouch",
        body = createInventoryBody("Pouch", size),
        properties = Properties(
            Values(
                SIZE to size.toString(),
                WEIGHT to weight.toString()
            ),
            Tags(ITEM_TAG, CONTAINER, OPEN, ITEM_TAG)
        )
    )
}

// Chest is a container
fun createChest(size: Int = 10): Thing {
    return Thing(
        "Chest", body = createInventoryBody("Chest", size),
        properties = Properties(
            Values(SIZE to size.toString()),
            Tags(CONTAINER, OPEN, ACTIVATOR_TAG)
        )
    )
}

fun createClosedChest(size: Int = 10): Thing {
    return Thing(
        "Closed Chest",
        body = createInventoryBody("Closed Chest", size),
        properties = Properties(
            Values(SIZE to size.toString()),
            Tags(CONTAINER, ACTIVATOR_TAG)
        )
    )
}

fun createPackMule(strength: Int = 1): Thing {
    return Thing(
        "Pack Mule", body = createInventoryBody("Pack Mule"),
        properties = Properties(
            Values(STRENGTH to strength.toString()),
            Tags(CONTAINER, OPEN, CREATURE_TAG)
        )
    )
}

fun createMockedGame() {
    DependencyInjector.clearAllImplementations()
    DependencyInjector.setImplementation(ActivatorsCollection::class, ActivatorsMock())
    ActivatorManager.reset()

    DependencyInjector.setImplementation(AIActionsCollection::class, AIActionsMock())
    DependencyInjector.setImplementation(AIsCollection::class, AIsMock())
    AIManager.reset()

    DependencyInjector.setImplementation(BehaviorsCollection::class, BehaviorsMock())
    BehaviorManager.reset()

    DependencyInjector.setImplementation(BodysCollection::class, BodysMock.withFakePlayer())
    DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
    BodyManager.reset()

    DependencyInjector.setImplementation(CommandsCollection::class, CommandsMock())

    DependencyInjector.setImplementation(ConditionsCollection::class, ConditionsGenerated())
    ConditionManager.reset()

    DependencyInjector.setImplementation(DialoguesCollection::class, DialoguesMock())

    DependencyInjector.setImplementation(EffectsCollection::class, EffectsMock())
    EffectManager.reset()

    DependencyInjector.setImplementation(EventListenerMapMock::class, EventListenerMapMock())
    EventManager.reset()

    DependencyInjector.setImplementation(ItemsCollection::class, ItemsMock())
    ItemManager.reset()

    DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
    DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
    LocationManager.reset()

    DependencyInjector.setImplementation(RecipesCollection::class, RecipesMock())
    RecipeManager.reset()

    DependencyInjector.setImplementation(StoryEventsCollection::class, StoryEventsMock())
    QuestManager.reset()

    DependencyInjector.setImplementation(SpellCommandsCollection::class, SpellCommandsMock())

    DependencyInjector.setImplementation(WeathersCollection::class, WeathersMock())
    WeatherManager.reset()

    EventManager.clear()
    GameState.reset()
    CommandParsers.reset()
    CommandParsers.setResponseRequest(GameState.player, null)

    GameLogger.reset()

}

