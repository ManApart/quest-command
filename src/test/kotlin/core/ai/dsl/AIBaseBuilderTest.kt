package core.ai.dsl

import core.DependencyInjector
import core.ai.AIManager
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionsCollection
import core.ai.action.dsl.AIActionsMock
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class AIBaseBuilderTest {
    @Before
    fun setup() {
        DependencyInjector.setImplementation(AIsCollection::class.java, AIsMock())
        val actions = AIActionsMock(listOf(AIAction("run"), AIAction("jump"), AIAction("swim")))
        DependencyInjector.setImplementation(AIActionsCollection::class.java, actions)
        AIManager.reset()
    }

    @Test
    fun happyPath() {
        val values = aiBuilder {
            ai("parent") {
                additional = listOf("run", "jump")
            }
            ai("child") {
                inherits = listOf("parent")
                additional = listOf("swim")
                exclude = listOf("run")
            }
        }.build()

        assertEquals(2, values.size)

        val parent = values.firstOrNull { it.name == "parent" }!!
        assertEquals(2, parent.actions.size)
        assertTrue(parent.actions.any { it.name == "run" })
        assertTrue(parent.actions.any { it.name == "jump" })

        val child = values.firstOrNull { it.name == "child" }!!
        assertEquals(2, child.actions.size)
        assertTrue(child.actions.any { it.name == "swim" })
        assertTrue(child.actions.any { it.name == "jump" })

    }

    @Test
    fun circularDependenciesAreFound() {
        val builder = aiBuilder {
            ai("parent") {
                inherits = listOf("child")
                additional = listOf("run", "jump")
            }
            ai("child") {
                inherits = listOf("parent")
                additional = listOf("swim")
                exclude = listOf("run")
            }
        }

        assertFails { builder.build() }
    }

}