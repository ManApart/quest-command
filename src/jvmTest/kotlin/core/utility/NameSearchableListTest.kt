package core.utility

import kotlin.test.Test
import kotlin.test.*

class NameSearchableListTest {

    @Test
    fun thingExistsFull() {
        val thing = NamedString("Full Match")
        val badThing = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val baseline = list.exists(badThing.name)
        val actual = list.exists(thing.name)

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun thingExistsPartial() {
        val thing = NamedString("Partial Match")
        val badThing = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val baseline = list.exists(badThing.name)
        val actual = list.exists("Partial")

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun existsExact() {
        val partial = NamedString("Partial Match")
        val exact = NamedString("Exact Match")

        val list = NameSearchableList<NamedString>()
        list.add(partial)
        list.add(exact)

        assertTrue(list.existsExact("Exact Match"))
        assertFalse(list.existsExact("Match"))
    }

    @Test
    fun existsByWholeWord() {
        val exact = NamedString("Exact Match")
        val partial = NamedString("Partial Match")

        val list = NameSearchableList<NamedString>()
        list.add(partial)
        list.add(exact)

        assertTrue(list.existsByWholeWord("match"))
        assertTrue(list.exists("at"))
        assertFalse(list.existsByWholeWord("at"))
    }

    @Test
    fun thingExistsPartialPrefersExactMatch() {
        val applePieTin = NamedString("apple pie Tin")
        val pieTin = NamedString("Pie Tin")
        val tinderBox = NamedString("Tinderbox")

        val list = NameSearchableList<NamedString>()
        list.add(tinderBox)
        list.add(applePieTin)
        list.add(pieTin)

        assertTrue(list.exists("apple pie tin"))
        assertEquals(applePieTin, list.getOrNull("apple pie tin"))
    }

    @Test
    fun thingExistsPartialPrefersWholeWord() {
        val pieTin = NamedString("Pie Tin")
        val tinderBox = NamedString("Tinderbox")

        val list = NameSearchableList<NamedString>()
        list.add(tinderBox)
        list.add(pieTin)

        assertTrue(list.exists("tin"))
        assertEquals(pieTin, list.getOrNull("tin"))
    }

    @Test
    fun blankThingDoesNotReturnResults() {
        val list = NameSearchableList<NamedString>()
        list.add(NamedString("Match"))

        assertFalse(list.exists(""))
        assertNull(list.getOrNull(""))
        assertTrue(list.getAll("").isEmpty())
        assertTrue(list.getAny(listOf("")).isEmpty())
    }

    @Test
    fun thingExistsUsingListFull() {
        val thing = NamedString("Full Match")
        val badThing = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val baseline = list.exists(badThing.name)
        val actual = list.exists(thing.name)

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun thingExistsUsingListPartial() {
        val thing = NamedString("Partial Match")
        val badThing = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val baseline = list.exists(badThing.name)
        val actual = list.exists("Partial")

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun getThingFull() {
        val thing = NamedString("Full Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = list.get(thing.name)

        assertEquals(thing, actual)
    }

    @Test
    fun getThingPartial() {
        val thing = NamedString("Partial Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = list.get("Partial")

        assertEquals(thing, actual)
    }

    @Test
    fun getThingUsingListFull() {
        val thing = NamedString("Full Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = list.get(thing.name)

        assertEquals(thing, actual)
    }

    @Test
    fun getThingUsingListPartial() {
        val thing = NamedString("Partial Match")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = list.get("Partial")

        assertEquals(thing, actual)
    }

    @Test
    fun getOrNullProxy() {
        val thing = NamedString("Thing")

        val list = NameSearchableList<NamedString>()
        list.addProxy(thing, listOf("proxy"))

        val actual = list.get("proxy")

        assertEquals(thing, actual)
    }

    @Test
    fun getOrNullProxyCaseInsensitive() {
        val thing = NamedString("Thing")

        val list = NameSearchableList<NamedString>()
        list.addProxy(thing, listOf("PROXY"))

        val actual = list.get("proxy")

        assertEquals(thing, actual)
    }

    @Test
    fun constructFromItem() {
        val thing = NamedString("Thing")

        val list = NameSearchableList(thing)
        assertEquals(1, list.size)
        assertEquals(thing, list.get("Thing"))
    }

    @Test
    fun constructFromIterable() {
        val thing = NamedString("Thing")
        val thing2 = NamedString("Thing2")

        val list = NameSearchableList(listOf(thing, thing2))
        assertEquals(2, list.size)
        assertEquals(thing, list.get("Thing"))
        assertEquals(thing2, list.get("Thing2"))

    }

    @Test
    fun constructFromNameSearchable() {
        val thing = NamedString("Thing")
        val thing2 = NamedString("Thing2")

        val firstList = NameSearchableList(thing)
        firstList.addProxy(thing2, listOf("proxy"))

        val list = NameSearchableList(firstList)

        assertEquals(2, list.size)
        assertEquals(thing, list.get("Thing"))
        assertEquals(thing2, list.get("proxy"))
    }

    @Test
    fun toList() {
        val thing = NamedString("Thing")

        val list = listOf(thing).toNameSearchableList()
        assertEquals(1, list.size)
        assertEquals(thing, list.get("Thing"))
    }

    @Test
    fun existsExactCleansString() {
        val thing = NamedString("Target")

        val list = listOf(thing).toNameSearchableList()
        assertTrue(list.existsExact(" TARGET "))
    }

    @Test
    fun existsExactMatchesProxy() {
        val thing = NamedString("Target")

        val list = listOf(thing).toNameSearchableList()
        list.addProxy(thing, "Carget")
        assertTrue(list.existsExact(" carget "))
    }

    @Test
    fun existsExactDoesNotReturnPartialMatch() {
        val thing = NamedString("Thing")

        val list = listOf(thing).toNameSearchableList()
        assertFalse(list.existsExact("tar"))
    }

    @Test
    fun removeThingAlsoRemovesProxy() {
        val thing = NamedString("Thing")

        val list = NameSearchableList<NamedString>()
        list.addProxy(thing, listOf("proxy"))

        val actual = list.get("proxy")
        assertEquals(thing, actual)
        list.remove(thing)
        assertNull(list.getOrNull(thing.name))
        assertNull(list.getOrNull("proxy"))
    }

    @Test
    fun getTriesWithSpaces() {
        val thing = NamedString("A Thing")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = list.get("AThing")
        assertEquals(thing, actual)
    }

    @Test
    fun equalsMethod() {
        val thing = NamedString("A Thing")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = NameSearchableList(thing)
        assertEquals(list, actual)
    }

    @Test
    fun differentProxiesStillEqual() {
        val thing = NamedString("A Thing")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = NameSearchableList(thing)
        actual.addProxy(thing, "Bob")
        assertEquals(list, actual)
    }

    @Test
    fun notEquals() {
        val thing = NamedString("A Thing")
        val thing2 = NamedString("Not A Thing")

        val list = NameSearchableList<NamedString>()
        list.add(thing)

        val actual = NameSearchableList(thing2)
        assertNotEquals(list, actual)
    }

}


