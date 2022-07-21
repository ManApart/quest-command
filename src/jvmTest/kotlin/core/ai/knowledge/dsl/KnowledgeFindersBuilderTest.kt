package core.ai.knowledge.dsl

import core.ai.knowledge.SimpleSubject
import core.ai.knowledge.Subject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/*
TODO Test
Relates to
Comparison
Context
 */

class KnowledgeFindersBuilderTest {

    @Test
    fun redSourceFact() {
        val facts = finders.first().getFacts(cardinal.mind, Subject(cardinal), "Red")

        assertEquals(1, facts.size)
        assertEquals("Red", facts.first().kind)
        assertEquals(100, facts.first().confidence)
        assertEquals(100, facts.first().amount)
    }

    @Test
    fun notRedSourceFact() {
        val facts = finders.first().getFacts(cardinal.mind, Subject(cat), "Red")

        assertEquals(1, facts.size)
        assertEquals("Red", facts.first().kind)
        assertEquals(100, facts.first().confidence)
        assertEquals(0, facts.first().amount)
    }

    @Test
    fun listFact() {
        val facts = finders.first().getListFacts(cardinal.mind, "Big")

        assertEquals(1, facts.size)
        assertEquals("Big", facts.first().kind)
        assertEquals(SimpleSubject("Dog"), facts.first().sources.first())
    }
}