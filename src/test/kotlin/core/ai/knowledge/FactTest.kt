package core.ai.knowledge

import core.thing.Thing
import kotlin.test.Test
import kotlin.test.assertEquals


class FactTest {

    @Test
    fun singleEntry() {
        val actual = average(6 to 10)
        assertEquals(6, actual.confidence)
        assertEquals(10, actual.amount)
    }

    @Test
    fun confidenceIsAveraged() {
        val actual = average(0 to 10, 10 to 1, 5 to 1)
        assertEquals(5, actual.confidence)
    }

    @Test
    fun amountIsWeightedAverage() {
        val actual = average(2 to 10, 5 to 50, 3 to 40)
        assertEquals(39, actual.amount)
    }

    private fun average(vararg facts: Pair<Int, Int>): Fact {
        val input = facts.map { (confidence, amount) -> fact(confidence, amount) }
        return input.average(input.first().source, input.first().kind)
    }

    private fun fact(confidence: Int = 0, amount: Int = 0) = Fact(Subject(Thing("Bob")), "test", confidence, amount)
}