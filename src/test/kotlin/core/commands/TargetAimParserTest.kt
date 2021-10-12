package core.commands

import assertContainsByName
import assertEqualsByName
import core.DependencyInjector
import core.GameState
import core.body.*
import core.target.Target
import core.target.target
import createMockedGame
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.networks
import kotlin.test.assertEquals

//TODO - use for more than just spells (attacks, interact etc)
//TODO - test inventory target parsing
class TargetAimParserTest {
    //Cast <word> <word args> on *<body part> of *<target>. NOT IMPLEMENTED"
    //val targets = parseTargets("cast ${spellCommand.name} ${spellArgs.fullString}", arguments)

    companion object {
        private val bodyPartA = LocationRecipe("bodyPartA")
        private val bodyPartB = LocationRecipe("bodyPartB")
        private val bodyPartC = LocationRecipe("bodyPartC")

        init {
            createMockedGame()

            val bodyCollection = BodysMock(
                networks {
                    network("Human") {
                        locationNode("bodyPartA")
                    }
                    network("testBody") {
                        locationNode("bodyPartA")
                        locationNode("bodyPartB")
                        locationNode("bodyPartC")
                    }
                }
            )
            val bodyPartCollection = BodyPartsMock(
                locations {
                    location("bodyPartA")
                    location("bodyPartB")
                    location("bodyPartC")
                }
            )

            DependencyInjector.setImplementation(BodysCollection::class, bodyCollection)
            DependencyInjector.setImplementation(BodyPartsCollection::class, bodyPartCollection)
            BodyManager.reset()
        }

        private val targetA = target("targetA") { body("testBody") }.build()
        private val targetB = target("targetB") { body("testBody") }.build()

        private val scope = GameState.player.target.currentLocation()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
        }

    }


    @Test
    fun noTarget() {
        val results = parseTargets(GameState.player.target, "".split(" "))
        assertEquals(0, results.size)
    }

    @Test
    fun singleTarget() {
        val results = parseTargets(GameState.player.target, "targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(targetA, results.first().target)
    }

    @Test
    fun singleTargetOneBodyPart() {
        val results = parseTargets(GameState.player.target, "bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(1, results.first().bodyPartTargets.size)

        assertEqualsByName(targetA, results.first().target)
        assertEqualsByName(bodyPartB, results.first().bodyPartTargets.first())
    }

    @Test
    fun singleTargetAllBodyParts() {
        val results = parseTargets(GameState.player.target, "all of targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(3, results.first().bodyPartTargets.size)

        assertEquals(targetA, results.first().target)
        val parts = results.first().bodyPartTargets
        assertContainsByName(parts, bodyPartA)
        assertContainsByName(parts, bodyPartB)
        assertContainsByName(parts, bodyPartC)
    }

    @Test
    fun singleTargetTwoBodyParts() {
        val results = parseTargets(GameState.player.target, "bodyPartA bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)

        val result = results.first()
        assertEquals(2, result.bodyPartTargets.size)
        assertEquals(targetA, result.target)
        assertEqualsByName(bodyPartA, result.bodyPartTargets.first())
        assertEqualsByName(bodyPartB, result.bodyPartTargets.last())
    }

    @Test
    fun multiTarget() {
        val results = parseTargets(GameState.player.target, "targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(targetB, results.last().target)
    }

    @Test
    fun multiTargetReverseOrder() {
        val results = parseTargets(GameState.player.target, "targetB and targetA".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.last().target)
        assertEquals(targetB, results.first().target)
    }

    @Test
    fun multiTargetOnBodyPart() {
        val results = parseTargets(GameState.player.target, "bodyPartC of targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(1, results.first().bodyPartTargets.size)
        assertEqualsByName(bodyPartC, results.first().bodyPartTargets.first())

        assertEquals(targetB, results.last().target)
        assertEquals(0, results.last().bodyPartTargets.size)
    }

    @Test
    fun multiTargetTwoBodyParts() {
        val results = parseTargets(GameState.player.target, "bodyPartA bodyPartB of targetA and bodyPartB bodyPartC of targetB".split(" "))

        assertEquals(2, results.size)

        val resultA = results.first()
        val resultB = results.last()

        assertEquals(targetA, resultA.target)
        assertEquals(2, resultA.bodyPartTargets.size)
        assertEqualsByName(bodyPartA, resultA.bodyPartTargets.first())
        assertEqualsByName(bodyPartB, resultA.bodyPartTargets.last())

        assertEquals(targetB, resultB.target)
        assertEquals(2, resultB.bodyPartTargets.size)
        assertEqualsByName(bodyPartB, resultB.bodyPartTargets.first())
        assertEqualsByName(bodyPartC, resultB.bodyPartTargets.last())
    }


}
