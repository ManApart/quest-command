package core.thing

import core.DependencyInjector
import core.ai.behavior.*
import core.body.*
import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.RIGHT_HAND
import core.properties.Properties
import core.properties.TagStrings.SMALL
import core.properties.Tags
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.LEATHER
import explore.look.LookEvent
import kotlinx.coroutines.runBlocking


import quests.ConditionalEvents
import resources.thing.burnable
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

//TODO - test extends

class ThingBuilderTest {

    @BeforeTest
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock(bodies {
            body("Dagger") {
                mat(IRON)
                part("Handle") {
                    mat(LEATHER)
                }
                parts("Pommel", "Guard", "Blade")
            }
            body("Human", RIGHT_HAND, LEFT_HAND)
            body("None", "Part")
        }))
        BodyManager.reset()

        val behaviorParser = BehaviorsMock(listOf(Behavior("Burnable", ConditionalEvents(LookEvent::class))))
        DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
        BehaviorManager.reset()
    }

    @Test
    fun basicBuild() {
        runBlocking {
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
    }

    @Test
    fun buildAnother() {
        runBlocking {
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

    @Test
    fun bodyExtends() {
        runBlocking {
            val (rusty, broken) = things {
                thing("Rusty Dagger") {
                    body("Dagger")
                    description("It once held a cutting edge, but those days are little remembered.")
                    equipToHoldOneHand()
                    props {
                        value("weight", 1)
                        value("slashDamage", 1)
                        value("stabDamage", 2)
                        value("range", 2)
                        tag("Weapon", "Sharp", SMALL)
                    }
                }
                thing("Broken Dagger") {
                    extends("Rusty Dagger")
                    description("The blade has broken off.")
                    body {
                        removePart("Blade")
                        part("Broken Blade")
                    }
                    props {
                        value("stabDamage", 1)
                        value("range", 1)
                    }
                }
            }.build()

            with(rusty) {
                assertEquals(2, properties.values.getInt("stabDamage"))
                assertNull(body.parts.getOrNull("Broken Blade"))
                assertNotNull(body.parts.get("Blade"))
            }

            with(broken) {
                assertEquals(1, properties.values.getInt("stabDamage"))
                assertEquals("Broken Blade", body.parts.getOrNull("Blade")?.name)
            }
        }
    }
}
