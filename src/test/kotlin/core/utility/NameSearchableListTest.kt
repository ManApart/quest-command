package core.utility

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NameSearchableListTest {

    @Test
    fun targetExistsFull() {
        val target = NamedString("Full Match")
        val badTarget = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists(target.name)

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun targetExistsPartial() {
        val target = NamedString("Partial Match")
        val badTarget = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists("Partial")

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun blankTargetDoesNotReturnResults() {
        val list = NameSearchableList<NamedString>()
        list.add(NamedString("Match"))

        assertFalse(list.exists(""))
        assertNull(list.getOrNull(""))
        assertTrue(list.getAll("").isEmpty())
        assertTrue(list.getAny(listOf("")).isEmpty())
    }

    @Test
    fun targetExistsUsingListFull() {
        val target = NamedString("Full Match")
        val badTarget = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists(target.name)

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun targetExistsUsingListPartial() {
        val target = NamedString("Partial Match")
        val badTarget = NamedString("No Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists("Partial")

        assertFalse(baseline)
        assertTrue(actual)
    }

    @Test
    fun getTargetFull() {
        val target = NamedString("Full Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val actual = list.get(target.name)

        assertEquals(target, actual)
    }

    @Test
    fun getTargetPartial() {
        val target = NamedString("Partial Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val actual = list.get("Partial")

        assertEquals(target, actual)
    }

    @Test
    fun getTargetUsingListFull() {
        val target = NamedString("Full Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val actual = list.get(target.name)

        assertEquals(target, actual)
    }

    @Test
    fun getTargetUsingListPartial() {
        val target = NamedString("Partial Match")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val actual = list.get("Partial")

        assertEquals(target, actual)
    }

    @Test
    fun getOrNullProxy() {
        val target = NamedString("Target")

        val list = NameSearchableList<NamedString>()
        list.addProxy(target, listOf("proxy"))

        val actual = list.get("proxy")

        assertEquals(target, actual)
    }

    @Test
    fun getOrNullProxyCaseInsensitive() {
        val target = NamedString("Target")

        val list = NameSearchableList<NamedString>()
        list.addProxy(target, listOf("PROXY"))

        val actual = list.get("proxy")

        assertEquals(target, actual)
    }


}


