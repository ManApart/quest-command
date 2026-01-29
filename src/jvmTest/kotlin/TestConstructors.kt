import conversation.dsl.DialoguesCollection
import conversation.dsl.DialoguesMock
import core.DependencyInjector
import core.GameState
import core.ai.AIPackageManager
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.*
import core.commands.CommandParsers
import core.commands.CommandsCollection
import core.commands.CommandsMock
import core.events.EventListenerMapCollection
import core.events.EventListenerMapMock
import core.events.EventManager
import core.history.GameLogger
import core.properties.Properties
import core.properties.TagKey
import core.properties.TagKey.CONTAINER
import core.properties.TagKey.ITEM
import core.properties.TagKey.OPEN
import core.properties.TagKey.SIZE
import core.properties.Tags
import core.properties.ValueKey.WEIGHT
import core.properties.Values
import core.thing.Thing
import core.thing.ThingBuilder
import core.thing.activator.ACTIVATOR_TAG
import core.thing.activator.ActivatorManager
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.activator.dsl.ActivatorsMock
import core.thing.item.ItemManager
import core.thing.item.ItemsCollection
import core.thing.item.ItemsMock
import core.thing.thing
import crafting.RecipeManager
import crafting.RecipesCollection
import crafting.RecipesMock
import crafting.material.MaterialManager
import crafting.material.MaterialsCollection
import crafting.material.MaterialsMock
import inventory.createInventoryBody
import kotlinx.coroutines.runBlocking
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
import status.stat.Attributes.STRENGTH
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import traveling.location.weather.WeatherManager
import traveling.location.weather.WeathersCollection
import traveling.location.weather.WeathersMock

fun createItem(name: String = "Apple", weight: Int = 1): Thing {
    return runBlocking { createItemBuilder(name, weight).build() }
}

fun createItemBuilder(name: String = "Apple", weight: Int = 1): ThingBuilder {
    return runBlocking {
        thing(name) {
            props {
                value("weight" to weight)
                tag(ITEM)
            }
        }
    }
}

//Pouch is a container that is also an item
fun createPouch(size: Int = 5, weight: Int = 1): Thing {
    return Thing(
        "Pouch",
        body = createInventoryBodyBlocking("Pouch", size),
        properties = Properties(
            Values(
                SIZE to size.toString(),
                WEIGHT to weight.toString()
            ),
            Tags(ITEM, CONTAINER, OPEN, ITEM)
        )
    )
}

// Chest is a container
fun createChest(size: Int = 10): Thing {
    return Thing(
        "Chest", body = createInventoryBodyBlocking("Chest", size),
        properties = Properties(
            Values(SIZE to size.toString()),
            Tags(CONTAINER, OPEN, ACTIVATOR_TAG)
        )
    )
}

fun createClosedChest(size: Int = 10): Thing {
    return Thing(
        "Closed Chest",
        body = createInventoryBodyBlocking("Closed Chest", size),
        properties = Properties(
            Values(SIZE to size.toString()),
            Tags(CONTAINER, ACTIVATOR_TAG)
        )
    )
}

fun createPackMule(strength: Int = 1): Thing {
    return Thing(
        "Pack Mule", body = createInventoryBodyBlocking("Pack Mule"),
        properties = Properties(
            Values(STRENGTH to strength.toString()),
            Tags(CONTAINER, OPEN, TagKey.CREATURE)
        )
    )
}


fun createInventoryBodyBlocking(name: String = "Inventory", capacity: Int? = null): Body {
    return runBlocking { createInventoryBody(name, capacity) }
}

fun createMockedGame() {
    runBlocking {
        DependencyInjector.clearAllImplementations()

        //Must go before bodies
        DependencyInjector.setImplementation(MaterialsCollection::class, MaterialsMock())
        MaterialManager.reset()

        DependencyInjector.setImplementation(ActivatorsCollection::class, ActivatorsMock())
        ActivatorManager.reset()

        AIPackageManager.reset()

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

        DependencyInjector.setImplementation(EventListenerMapCollection::class, EventListenerMapMock())
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
        runBlocking { GameState.player.location.getLocation().addThing(GameState.player.thing) }

        GameLogger.reset()
    }
}
