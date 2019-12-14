package core.gamestate.dataParsing

import quests.triggerCondition.Operator
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OperatorTest {

    @Test
    fun equalsStringPositive() {
        val left = "VALUE"
        val right = "value"
        assertTrue(Operator.EQUALS.evaluate(left, right))
    }

    @Test
    fun equalsStringNegative() {
        val left = "value"
        val right = "different"
        assertFalse(Operator.EQUALS.evaluate(left, right))
    }

    @Test
    fun equalsIntPositive() {
        val left = "1"
        val right = "1"
        assertTrue(Operator.EQUALS.evaluate(left, right))
    }

    @Test
    fun equalsIntNegative() {
        val left = "1"
        val right = "2"
        assertFalse(Operator.EQUALS.evaluate(left, right))
    }

    @Test
    fun notEqualsStringPositive() {
        val left = "value"
        val right = "different"
        assertTrue(Operator.NOT_EQUALS.evaluate(left, right))
    }

    @Test
    fun notEqualsStringNegative() {
        val left = "value"
        val right = "VALUE"
        assertFalse(Operator.NOT_EQUALS.evaluate(left, right))
    }

    @Test
    fun notEqualsIntPositive() {
        val left = "1"
        val right = "2"
        assertTrue(Operator.NOT_EQUALS.evaluate(left, right))
    }

    @Test
    fun notEqualsIntNegative() {
        val left = "1"
        val right = "1"
        assertFalse(Operator.NOT_EQUALS.evaluate(left, right))
    }

    @Test
    fun greaterThanIntPositive() {
        val left = "2"
        val right = "1"
        assertTrue(Operator.GREATER_THAN.evaluate(left, right))
    }

    @Test
    fun greaterThanIntNegative() {
        val left = "1"
        val right = "1"
        assertFalse(Operator.GREATER_THAN.evaluate(left, right))
    }

    @Test
    fun greaterThanEqualToIntPositive() {
        val left = "2"
        val right = "1"
        assertTrue(Operator.GREATER_THAN_EQUALS_TO.evaluate(left, right))
    }

    @Test
    fun greaterThanEqualToIntPositive2() {
        val left = "1"
        val right = "1"
        assertTrue(Operator.GREATER_THAN_EQUALS_TO.evaluate(left, right))
    }

    @Test
    fun greaterThanEqualToIntNegative() {
        val left = "1"
        val right = "2"
        assertFalse(Operator.GREATER_THAN_EQUALS_TO.evaluate(left, right))
    }

    @Test
    fun containsStringPositive() {
        val left = "my,thing,has,value,i,hope"
        val right = "value"
        assertTrue(Operator.CONTAINS.evaluate(left, right))
    }

    @Test
    fun containsStringNegative() {
        val left = "value"
        val right = "my,thing,has,value,i,hope"
        assertFalse(Operator.CONTAINS.evaluate(left, right))
    }

}