package core.dataParsing

import core.gamestate.dataParsing.TriggerConditionFakeEvent
import org.junit.Test
import quests.triggerCondition.TriggerCondition
import kotlin.test.assertTrue

class TriggerConditionTest {

    @Test
    fun parseTypicalData() {
        val event = TriggerConditionFakeEvent()
        val condition = TriggerCondition("TriggerConditionFakeEvent", mapOf(
                "stringVal" to "StringValue",
                "intVal" to "0",
                "boolVal" to "false",
                "classVal" to "ABOVE"
        ))
        assertTrue(condition.matches(event))
    }



}