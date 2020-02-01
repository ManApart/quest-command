package quests.triggerCondition

import org.junit.Test
import kotlin.test.assertTrue

class ConditionTest {
    @Test
    fun emptyConditionReturnsTrue(){
        assertTrue(Condition().matches(mapOf()))
    }
}