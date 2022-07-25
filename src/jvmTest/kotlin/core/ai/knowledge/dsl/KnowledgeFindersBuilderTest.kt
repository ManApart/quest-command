package core.ai.knowledge.dsl

import core.ai.knowledge.Subject
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/*
TODO Test
Comparison
Context
 */

class KnowledgeFindersBuilderTest {

    @Test
    fun redSourceFact() {
        val facts = colorFinders.first().getFacts(cardinal.mind, Subject(cardinal), "Red")

        assertEquals(1, facts.size)
        assertEquals("Red", facts.first().kind)
        assertEquals(100, facts.first().confidence)
        assertEquals(100, facts.first().amount)
    }

    @Test
    fun notRedSourceFact() {
        val facts = colorFinders.first().getFacts(cardinal.mind, Subject(cat), "Red")

        assertEquals(1, facts.size)
        assertEquals("Red", facts.first().kind)
        assertEquals(100, facts.first().confidence)
        assertEquals(0, facts.first().amount)
    }

    @Test
    fun listFactOnlyLooksAtKind() {
        val facts = colorFinders.first().getListFacts(cardinal.mind, "Big")

        assertEquals(2, facts.size)
        assertEquals("Big", facts.first().kind)
        val sources = facts.flatMap { it.sources.map { source -> source.topic } }
        assertEquals(listOf("Dog", "bird"), sources)
    }

    @Test
    fun relatesToEat() {
        val relationship = relationFinders.first().getRelationships(cat.mind, Subject(cat), "Eats", Subject(cardinal)).first { it.kind == "Eats" }

        assertEquals(90, relationship.confidence)
        assertEquals(100, relationship.amount)
    }

    @Test
    fun relatesToFlee() {
        val relationships = relationFinders.first().getRelationships(cardinal.mind, Subject(cardinal), "Flees", Subject(cat)).first { it.kind == "Flees" }

        assertEquals(100, relationships.confidence)
        assertEquals(99, relationships.amount)
    }

    @Test
    fun nestedRelates() {
        val relationships = colorFinders.first().getRelationships(cardinal.mind, Subject(cardinal), "Big", Subject(cat))

        assertEquals(1, relationships.size)
        val relationship = relationships.first()

        assertEquals(10, relationship.confidence)
        assertEquals(65, relationship.amount)
    }
}