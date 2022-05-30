package core.thing

import core.DependencyInjector
import core.ai.behavior.*
import core.body.*
import core.properties.Properties
import core.properties.Tags
import explore.look.LookEvent



import quests.ConditionalEvents
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

//TODO - test extends

class ThingBuilderTest {

    @BeforeTest
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        val behaviorParser = BehaviorsMock(listOf(Behavior("Burnable", ConditionalEvents(LookEvent::class))))
        DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
        BehaviorManager.reset()
    }

    @Test
    fun basicBuild() {
        val behaviors = listOf(BehaviorRecipe("Burnable", mapOf("fireHealth" to "1"))).map { BehaviorManager.getBehavior(it) }
        val expected = Thing(
            "Bob",
            params = mapOf("this" to "that"),
            body = BodyManager.getBody("Human"),
            description = "A normal dude",
            behaviors = behaviors,
            properties = Properties(Tags("Person"))
        )

        val actual = thing("Bob") {
            description("A normal dude")
            param("this" to "that")
            body("human")
            props {
                tag("Person")
            }
            behavior("Burnable", "fireHealth" to 1)
        }.build()

        assertEquals(expected, actual)
    }

    @Test
    fun buildAnother() {
        val expected = Thing(
            "Jim",
            params = mapOf("another" to "thing"),
            body = BodyManager.getBody("Human"),
            description = "A fine fellow",
            properties = Properties(Tags("Warrior"))
        )

        val actual = thing("Jim") {
            description("A fine fellow")
            param("another" to "thing")
            body("Human")
            props {
                tag("Warrior")
            }
        }.build()

        assertEquals(expected, actual)
    }
}