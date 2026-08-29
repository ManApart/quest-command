package core.commands

import kotlin.test.Test
import assertContainsByName
import assertEqualsByName
import core.DependencyInjector
import core.GameState
import core.body.*
import core.thing.thing
import createMockedGame
import kotlinx.coroutines.runBlocking


import traveling.location.location.LocationRecipe
import kotlin.test.assertEquals

//TODO - use for more than just spells (attacks, interact etc)
//TODO - test inventory thing parsing
class ThingAimParserTest {
    //Cast <word> <word args> on *<body part> of *<thing>. NOT IMPLEMENTED"
    //val things = parseThings("cast ${spellCommand.name} ${spellArgs.fullString}", arguments)

    companion object {
        private val bodyPartA = LocationRecipe("partA")
        private val bodyPartB = LocationRecipe("partB")
        private val bodyPartC = LocationRecipe("partC")

        init {
            createMockedGame()

            val bodyCollection = BodysMock(
                bodies {
                    body("Human") {
                        parts("partA")
                    }
                    body("testBody") {
                        parts("partA", "partB", "partC")
                    }
                }
            )

            DependencyInjector.setImplementation(BodysCollection::class, bodyCollection)
            BodyManager.reset()
        }

        private val thingA = runBlocking { thing("thingA") { body("testBody") }.build() }
        private val thingB = runBlocking { thing("thingB") { body("testBody") }.build() }

        private val scope = runBlocking { GameState.player.thing.currentLocation() }

        init {
            scope.addThing(thingA)
            scope.addThing(thingB)
        }

    }


    @Test
    fun noThing() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "".split(" "))
            assertEquals(0, results.size)
        }
    }

    @Test
    fun singleThing() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "thingA".split(" "))

            assertEquals(1, results.size)
            assertEquals(thingA, results.first().thing)
        }
    }

    @Test
    fun singleThingOneBodyPart() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "partB of thingA".split(" "))

            assertEquals(1, results.size)
            assertEquals(1, results.first().parts.size)

            assertEqualsByName(thingA, results.first().thing)
            assertEqualsByName(bodyPartB, results.first().parts.first())
        }
    }

    @Test
    fun singleThingAllBodyParts() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "all of thingA".split(" "))

            assertEquals(1, results.size)
            assertEquals(3, results.first().parts.size)

            assertEquals(thingA, results.first().thing)
            val parts = results.first().parts
            assertContainsByName(parts, bodyPartA)
            assertContainsByName(parts, bodyPartB)
            assertContainsByName(parts, bodyPartC)
        }
    }

    @Test
    fun singleThingAllBodyPartsByBody() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "body of thingA".split(" "))

            assertEquals(1, results.size)
            assertEquals(3, results.first().parts.size)

            assertEquals(thingA, results.first().thing)
            val parts = results.first().parts
            assertContainsByName(parts, bodyPartA)
            assertContainsByName(parts, bodyPartB)
            assertContainsByName(parts, bodyPartC)
        }
    }

    @Test
    fun singleThingTwoBodyParts() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "partA partB of thingA".split(" "))

            assertEquals(1, results.size)

            val result = results.first()
            assertEquals(2, result.parts.size)
            assertEquals(thingA, result.thing)
            assertEqualsByName(bodyPartA, result.parts.first())
            assertEqualsByName(bodyPartB, result.parts.last())
        }
    }

    @Test
    fun multiThing() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "thingA and thingB".split(" "))

            assertEquals(2, results.size)
            assertEquals(thingA, results.first().thing)
            assertEquals(thingB, results.last().thing)
        }
    }

    @Test
    fun multiThingReverseOrder() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "thingB and thingA".split(" "))

            assertEquals(2, results.size)
            assertEquals(thingA, results.last().thing)
            assertEquals(thingB, results.first().thing)
        }
    }

    @Test
    fun multiThingOnBodyPart() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "partC of thingA and thingB".split(" "))

            assertEquals(2, results.size)
            assertEquals(thingA, results.first().thing)
            assertEquals(1, results.first().parts.size)
            assertEqualsByName(bodyPartC, results.first().parts.first())

            assertEquals(thingB, results.last().thing)
            assertEquals(0, results.last().parts.size)
        }
    }

    @Test
    fun multiThingTwoBodyParts() {
        runBlocking {
            val results = parseThingsFromLocation(GameState.player.thing, "partA partB of thingA and partB partC of thingB".split(" "))

            assertEquals(2, results.size)

            val resultA = results.first()
            val resultB = results.last()

            assertEquals(thingA, resultA.thing)
            assertEquals(2, resultA.parts.size)
            assertEqualsByName(bodyPartA, resultA.parts.first())
            assertEqualsByName(bodyPartB, resultA.parts.last())

            assertEquals(thingB, resultB.thing)
            assertEquals(2, resultB.parts.size)
            assertEqualsByName(bodyPartB, resultB.parts.first())
            assertEqualsByName(bodyPartC, resultB.parts.last())
        }
    }


}
