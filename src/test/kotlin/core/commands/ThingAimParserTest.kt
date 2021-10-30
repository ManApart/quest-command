package core.commands

import assertContainsByName
import assertEqualsByName
import core.DependencyInjector
import core.GameState
import core.body.*
import core.thing.thing
import createMockedGame
import org.junit.Test
import traveling.location.location.LocationRecipe
import traveling.location.location.locations
import traveling.location.network.networks
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
                networks {
                    network("Human") {
                        locationNode("partA")
                    }
                    network("testBody") {
                        locationNode("partA")
                        locationNode("partB")
                        locationNode("partC")
                    }
                }
            )
            val bodyPartCollection = BodyPartsMock(
                locations {
                    location("partA")
                    location("partB")
                    location("partC")
                }
            )

            DependencyInjector.setImplementation(BodysCollection::class, bodyCollection)
            DependencyInjector.setImplementation(BodyPartsCollection::class, bodyPartCollection)
            BodyManager.reset()
        }

        private val thingA = thing("thingA") { body("testBody") }.build()
        private val thingB = thing("thingB") { body("testBody") }.build()

        private val scope = GameState.player.thing.currentLocation()

        init {
            scope.addThing(thingA)
            scope.addThing(thingB)
        }

    }


    @Test
    fun noThing() {
        val results = parseThingsFromLocation(GameState.player.thing, "".split(" "))
        assertEquals(0, results.size)
    }

    @Test
    fun singleThing() {
        val results = parseThingsFromLocation(GameState.player.thing, "thingA".split(" "))

        assertEquals(1, results.size)
        assertEquals(thingA, results.first().thing)
    }

    @Test
    fun singleThingOneBodyPart() {
        val results = parseThingsFromLocation(GameState.player.thing, "partB of thingA".split(" "))

        assertEquals(1, results.size)
        assertEquals(1, results.first().bodyPartThings.size)

        assertEqualsByName(thingA, results.first().thing)
        assertEqualsByName(bodyPartB, results.first().bodyPartThings.first())
    }

    @Test
    fun singleThingAllBodyParts() {
        val results = parseThingsFromLocation(GameState.player.thing, "all of thingA".split(" "))

        assertEquals(1, results.size)
        assertEquals(3, results.first().bodyPartThings.size)

        assertEquals(thingA, results.first().thing)
        val parts = results.first().bodyPartThings
        assertContainsByName(parts, bodyPartA)
        assertContainsByName(parts, bodyPartB)
        assertContainsByName(parts, bodyPartC)
    }

    @Test
    fun singleThingAllBodyPartsByBody() {
        val results = parseThingsFromLocation(GameState.player.thing, "body of thingA".split(" "))

        assertEquals(1, results.size)
        assertEquals(3, results.first().bodyPartThings.size)

        assertEquals(thingA, results.first().thing)
        val parts = results.first().bodyPartThings
        assertContainsByName(parts, bodyPartA)
        assertContainsByName(parts, bodyPartB)
        assertContainsByName(parts, bodyPartC)
    }

    @Test
    fun singleThingTwoBodyParts() {
        val results = parseThingsFromLocation(GameState.player.thing, "partA partB of thingA".split(" "))

        assertEquals(1, results.size)

        val result = results.first()
        assertEquals(2, result.bodyPartThings.size)
        assertEquals(thingA, result.thing)
        assertEqualsByName(bodyPartA, result.bodyPartThings.first())
        assertEqualsByName(bodyPartB, result.bodyPartThings.last())
    }

    @Test
    fun multiThing() {
        val results = parseThingsFromLocation(GameState.player.thing, "thingA and thingB".split(" "))

        assertEquals(2, results.size)
        assertEquals(thingA, results.first().thing)
        assertEquals(thingB, results.last().thing)
    }

    @Test
    fun multiThingReverseOrder() {
        val results = parseThingsFromLocation(GameState.player.thing, "thingB and thingA".split(" "))

        assertEquals(2, results.size)
        assertEquals(thingA, results.last().thing)
        assertEquals(thingB, results.first().thing)
    }

    @Test
    fun multiThingOnBodyPart() {
        val results = parseThingsFromLocation(GameState.player.thing, "partC of thingA and thingB".split(" "))

        assertEquals(2, results.size)
        assertEquals(thingA, results.first().thing)
        assertEquals(1, results.first().bodyPartThings.size)
        assertEqualsByName(bodyPartC, results.first().bodyPartThings.first())

        assertEquals(thingB, results.last().thing)
        assertEquals(0, results.last().bodyPartThings.size)
    }

    @Test
    fun multiThingTwoBodyParts() {
        val results = parseThingsFromLocation(GameState.player.thing, "partA partB of thingA and partB partC of thingB".split(" "))

        assertEquals(2, results.size)

        val resultA = results.first()
        val resultB = results.last()

        assertEquals(thingA, resultA.thing)
        assertEquals(2, resultA.bodyPartThings.size)
        assertEqualsByName(bodyPartA, resultA.bodyPartThings.first())
        assertEqualsByName(bodyPartB, resultA.bodyPartThings.last())

        assertEquals(thingB, resultB.thing)
        assertEquals(2, resultB.bodyPartThings.size)
        assertEqualsByName(bodyPartB, resultB.bodyPartThings.first())
        assertEqualsByName(bodyPartC, resultB.bodyPartThings.last())
    }


}
