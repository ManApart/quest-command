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
import core.commands.CommandParser
import core.commands.CommandsCollection
import core.commands.CommandsMock
import core.events.EventListenersCollection
import core.events.EventListenersMock
import core.events.EventManager
import core.properties.*
import core.target.Target
import core.target.TargetBuilder
import core.target.activator.ACTIVATOR_TAG
import core.target.activator.ActivatorManager
import core.target.activator.dsl.ActivatorsCollection
import core.target.activator.dsl.ActivatorsMock
import core.target.creature.CREATURE_TAG
import core.target.item.ITEM_TAG
import core.target.item.ItemManager
import core.target.item.ItemsCollection
import core.target.item.ItemsMock
import core.target.target
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
import traveling.location.location.*
import traveling.location.network.NetworksCollection
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeathersCollection
import traveling.location.weather.WeathersMock

fun createItem(name: String = "Apple", weight: Int = 1): Target {
    return createItemBuilder(name, weight).build()
}

fun createItemBuilder(name: String = "Apple", weight: Int = 1): TargetBuilder {
    return target(name) {
        props {
            value("weight" to weight)
            tag(ITEM_TAG)
        }
    }
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
    DependencyInjector.setImplementation(ActivatorsCollection::class.java, ActivatorsMock())
    ActivatorManager.reset()

    DependencyInjector.setImplementation(AIActionsCollection::class.java, AIActionsMock())
    DependencyInjector.setImplementation(AIsCollection::class.java, AIsMock())
    AIManager.reset()

    DependencyInjector.setImplementation(BehaviorsCollection::class.java, BehaviorsMock())
    BehaviorManager.reset()

    DependencyInjector.setImplementation(BodysCollection::class.java, BodysMock.withFakePlayer())
    DependencyInjector.setImplementation(BodyPartsCollection::class.java, BodyPartsMock())
    BodyManager.reset()

    DependencyInjector.setImplementation(CommandsCollection::class.java, CommandsMock())

    DependencyInjector.setImplementation(ConditionsCollection::class.java, ConditionsGenerated())
    ConditionManager.reset()

    DependencyInjector.setImplementation(DialoguesCollection::class.java, DialoguesMock())

    DependencyInjector.setImplementation(EffectsCollection::class.java, EffectsMock())
    EffectManager.reset()

    DependencyInjector.setImplementation(EventListenersCollection::class.java, EventListenersMock())
    EventManager.reset()

    DependencyInjector.setImplementation(ItemsCollection::class.java, ItemsMock())
    ItemManager.reset()

    DependencyInjector.setImplementation(NetworksCollection::class.java, NetworksMock())
    DependencyInjector.setImplementation(LocationsCollection::class.java, LocationsMock())
    LocationManager.reset()

    DependencyInjector.setImplementation(RecipesCollection::class.java, RecipesMock())
    RecipeManager.reset()

    DependencyInjector.setImplementation(StoryEventsCollection::class.java, StoryEventsMock())
    QuestManager.reset()

    DependencyInjector.setImplementation(SpellCommandsCollection::class.java, SpellCommandsMock())

    DependencyInjector.setImplementation(WeathersCollection::class.java, WeathersMock())
    WeatherManager.reset()

    EventManager.clear()
    CommandParser.setResponseRequest(null)
    GameState.player = GameManager.newPlayer()

}

