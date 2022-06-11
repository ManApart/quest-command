package core.utility

import kotlin.test.Test
import kotlin.test.assertEquals

import kotlin.test.assertTrue

class NameSearchableListAnyTest {

    @Test
    fun getAnyUnique() {
        val thingA = NamedString("ItemA")
        val thingB = NamedString("ItemB")
        val thingC = NamedString("ItemC")

        val list = NameSearchableList<NamedString>()
        list.add(thingA)
        list.add(thingB)
        list.add(thingC)

        val results = list.getAny(listOf("ItemA", "itemb"))

        assertEquals(2, results.size)
        assertTrue(results.contains(thingA))
        assertTrue(results.contains(thingB))
    }

    @Test
    fun getAnyOverlapReturnsAsManyAsMatch() {
        val thingA = NamedString("Left Hand")
        val thingB = NamedString("Right Hand")
        val thingC = NamedString("Chest")

        val list = NameSearchableList<NamedString>()
        list.add(thingA)
        list.add(thingB)
        list.add(thingC)

        val results = list.getAny(listOf("hand"))

        assertEquals(2, results.size)
        assertTrue(results.contains(thingA))
        assertTrue(results.contains(thingB))
    }

    @Test
    fun getAnyOverlapPrefersGroupedArgs() {
        val thingA = NamedString("Left Hand")
        val thingB = NamedString("Right Hand")
        val thingC = NamedString("Chest")

        val list = NameSearchableList<NamedString>()
        list.add(thingA)
        list.add(thingB)
        list.add(thingC)

        val results = list.getAny(listOf("left", "hand", "chest"))

        assertEquals(2, results.size)
        assertTrue(results.contains(thingA))
        assertTrue(results.contains(thingC))
    }

    @Test
    fun getAnyOverlapPrefersGroupedArgs2() {
        val thingA = NamedString("Left Hand")
        val thingB = NamedString("Right Hand")
        val thingC = NamedString("hand")

        val list = NameSearchableList<NamedString>()
        list.add(thingA)
        list.add(thingB)
        list.add(thingC)

        val results = list.getAny(listOf("left", "hand", "right hand"))

        assertEquals(2, results.size)
        assertTrue(results.contains(thingA))
        assertTrue(results.contains(thingB))
    }
}