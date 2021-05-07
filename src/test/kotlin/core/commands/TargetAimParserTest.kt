package core.commands

import assertContainsByName
import assertEqualsByName
import core.DependencyInjector
import core.GameState
import core.body.BodyManager
import core.target.Target
import injectAllDefaultMocks
import org.junit.Test
import system.BodyFakeParser
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser
import traveling.location.location.LocationRecipe
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
            injectAllDefaultMocks()

            val bodyParser = BodyFakeParser(
                    listOf(
                            LocationNode(name = "bodyPartA", parent = "Human"),
                            LocationNode(name = "bodyPartA", parent = "testBody"),
                            LocationNode(name = "bodyPartB", parent = "testBody"),
                            LocationNode(name = "bodyPartC", parent = "testBody")
                    ),
                    listOf(
                            bodyPartA,
                            bodyPartB,
                            bodyPartC
                    )
            )

            DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
            BodyManager.reset()
        }

        private val targetA = Target("targetA", bodyName = "testBody")
        private val targetB = Target("targetB", bodyName = "testBody")

        private val scope = GameState.currentLocation()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
        }

    }



    @Test
    fun noTarget() {
        val results = parseTargets("".split(" "))
        assertEquals(0, results.size)
    }

    @Test
    fun singleTarget() {
        val results = parseTargets("targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(targetA, results.first().target)
    }

    @Test
    fun singleTargetOneBodyPart() {
        val results = parseTargets("bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(1, results.first().bodyPartTargets.size)

        assertEqualsByName(targetA, results.first().target)
        assertEqualsByName(bodyPartB, results.first().bodyPartTargets.first())
    }

    @Test
    fun singleTargetAllBodyParts() {
        val results = parseTargets("all of targetA".split(" "))

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
        val results = parseTargets("bodyPartA bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)

        val result = results.first()
        assertEquals(2, result.bodyPartTargets.size)
        assertEquals(targetA, result.target)
        assertEqualsByName(bodyPartA, result.bodyPartTargets.first())
        assertEqualsByName(bodyPartB, result.bodyPartTargets.last())
    }

    @Test
    fun multiTarget() {
        val results = parseTargets("targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(targetB, results.last().target)
    }

    @Test
    fun multiTargetReverseOrder() {
        val results = parseTargets("targetB and targetA".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.last().target)
        assertEquals(targetB, results.first().target)
    }

    @Test
    fun multiTargetOnBodyPart() {
        val results = parseTargets("bodyPartC of targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(1, results.first().bodyPartTargets.size)
        assertEqualsByName(bodyPartC, results.first().bodyPartTargets.first())

        assertEquals(targetB, results.last().target)
        assertEquals(0, results.last().bodyPartTargets.size)
    }

    @Test
    fun multiTargetTwoBodyParts() {
        val results = parseTargets("bodyPartA bodyPartB of targetA and bodyPartB bodyPartC of targetB".split(" "))

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
