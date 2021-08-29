package core.target

import core.ai.behavior.BehaviorRecipe
import core.conditional.ConditionalStringType
import org.junit.Test

//TODO - asserts on basics
//TODO - test extends

class TargetBuilderTest {
    @Test
    fun basicBuild() {
        target("Bob"){
            description("A normal dude")
            params("this" to "that")
            props {
                tag("Person")
            }
            behavior("Burnable", "fireHealth" to 1)
        }.build()
    }

    @Test
    fun buildAnother() {
        target("Jim"){
            description("A normal dude", ConditionalStringType.LOCATION_DESCRIPTION)
            params("this" to "that")
            props {
                tag("Person")
            }
            behavior(
                BehaviorRecipe("Burnable", mapOf())
            )
        }.build()
    }
}