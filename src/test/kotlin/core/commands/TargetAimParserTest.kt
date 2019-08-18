package core.commands

import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.location.LocationNode
import core.utility.reflection.MockReflections
import core.utility.reflection.Reflections
import interact.scope.ScopeManager
import org.junit.AfterClass
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.DependencyInjector
import system.behavior.BehaviorParser
import system.body.BodyManager
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertTrue

//TODO - use for more than just spells (attacks, interact etc)
class TargetAimParserTest {
    //Cast <word> <word args> on *<body part> of *<target>. NOT IMPLEMENTED"
    //val targets = parseTargets("cast ${spellCommand.name} ${spellArgs.fullString}", arguments)

    companion object {
        private val bodyPartA = BodyPart("bodyPartA")
        private val bodyPartB = BodyPart("bodyPartB")
        private val bodyPartC = BodyPart("bodyPartC")

        init {
            DependencyInjector.setImplementation(BehaviorParser::class.java, BehaviorFakeParser())
            DependencyInjector.setImplementation(Reflections::class.java, MockReflections())
            DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())

            val bodyParser = BodyFakeParser(
                    listOf(
                            LocationNode(parent = "Human", name = "bodyPartA"),
                            LocationNode(parent = "testBody", name = "bodyPartA"),
                            LocationNode(parent = "testBody", name = "bodyPartB"),
                            LocationNode(parent = "testBody", name = "bodyPartC")
                    ),
                    listOf(
                            bodyPartA,
                            bodyPartB,
                            bodyPartC
                    )
            )

            DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
            BodyManager.reset()
        }

        private val targetA = Target("targetA", body = "testBody")
        private val targetB = Target("targetB", body = "testBody")
        private val scope = ScopeManager.getScope()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
            DependencyInjector.clearImplementation(BehaviorParser::class.java)
            DependencyInjector.clearImplementation(LocationParser::class.java)
            DependencyInjector.clearImplementation(BodyParser::class.java)
        }
    }

    @Test
    fun noTarget() {
        val results = parseTargets("", "".split(" "))
        assertEquals(0, results.size)
    }

    @Test
    fun singleTarget() {
        val results = parseTargets("", "targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(targetA, results.first().target)
    }

    @Test
    fun singleTargetOneBodyPart() {
        val results = parseTargets("", "bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(1, results.first().bodyPartTargets.size)

        assertEquals(targetA, results.first().target)
        assertEquals(bodyPartB, results.first().bodyPartTargets.first())
    }

    @Test
    fun singleTargetAllBodyParts() {
        val results = parseTargets("", "all of targetA".split(" "))

        assertEquals(1, results.size)
        assertEquals(3, results.first().bodyPartTargets.size)

        assertEquals(targetA, results.first().target)
        val parts = results.first().bodyPartTargets
        assertTrue(parts.contains(bodyPartA))
        assertTrue(parts.contains(bodyPartB))
        assertTrue(parts.contains(bodyPartC))
    }

    @Test
    fun singleTargetTwoBodyParts() {
        val results = parseTargets("", "bodyPartA bodyPartB of targetA".split(" "))

        assertEquals(1, results.size)

        val result = results.first()
        assertEquals(2, result.bodyPartTargets.size)
        assertEquals(targetA, result.target)
        assertEquals(bodyPartA, result.bodyPartTargets.first())
        assertEquals(bodyPartB, result.bodyPartTargets.last())
    }

    @Test
    fun multiTarget() {
        val results = parseTargets("", "targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(targetB, results.last().target)
    }

    @Test
    fun multiTargetReverseOrder() {
        val results = parseTargets("", "targetB and targetA".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.last().target)
        assertEquals(targetB, results.first().target)
    }

    @Test
    fun multiTargetOnBodyPart() {
        val results = parseTargets("", "bodyPartC of targetA and targetB".split(" "))

        assertEquals(2, results.size)
        assertEquals(targetA, results.first().target)
        assertEquals(1, results.first().bodyPartTargets.size)
        assertEquals(bodyPartC, results.first().bodyPartTargets.first())

        assertEquals(targetB, results.last().target)
        assertEquals(0, results.last().bodyPartTargets.size)
    }

    @Test
    fun multiTargetTwoBodyParts() {
        val results = parseTargets("", "bodyPartA bodyPartB of targetA and bodyPartB bodyPartC of targetB".split(" "))

        assertEquals(2, results.size)

        val resultA = results.first()
        val resultB = results.last()

        assertEquals(targetA, resultA.target)
        assertEquals(2, resultA.bodyPartTargets.size)
        assertEquals(bodyPartA, resultA.bodyPartTargets.first())
        assertEquals(bodyPartB, resultA.bodyPartTargets.last())

        assertEquals(targetB, resultB.target)
        assertEquals(2, resultB.bodyPartTargets.size)
        assertEquals(bodyPartB, resultB.bodyPartTargets.first())
        assertEquals(bodyPartC, resultB.bodyPartTargets.last())
    }

//    @Test
//    fun clarification() {
//
//    }

}