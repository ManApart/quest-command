package core.utility

import org.junit.Assert
import org.junit.Test
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

        Assert.assertEquals(2, results.size)
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

        Assert.assertEquals(2, results.size)
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

        Assert.assertEquals(2, results.size)
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

        Assert.assertEquals(2, results.size)
        assertTrue(results.contains(thingA))
        assertTrue(results.contains(thingB))
    }
}