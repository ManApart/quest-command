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
    fun targetExistsPartialPrefersExactMatch() {
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
    fun targetExistsPartialPrefersWholeWord() {
        val pieTin = NamedString("Pie Tin")
        val tinderBox = NamedString("Tinderbox")

        val list = NameSearchableList<NamedString>()
        list.add(tinderBox)
        list.add(pieTin)

        assertTrue(list.exists("tin"))
        assertEquals(pieTin, list.getOrNull("tin"))
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

    @Test
    fun constructFromItem() {
        val target = NamedString("Target")

        val list = NameSearchableList(target)
        assertEquals(1, list.size)
        assertEquals(target, list.get("Target"))
    }

    @Test
    fun constructFromIterable() {
        val target = NamedString("Target")
        val target2 = NamedString("Target2")

        val list = NameSearchableList(listOf(target, target2))
        assertEquals(2, list.size)
        assertEquals(target, list.get("Target"))
        assertEquals(target2, list.get("Target2"))

    }

    @Test
    fun constructFromNameSearchable() {
        val target = NamedString("Target")
        val target2 = NamedString("Target2")

        val firstList = NameSearchableList(target)
        firstList.addProxy(target2, listOf("proxy"))

        val list = NameSearchableList(firstList)

        assertEquals(2, list.size)
        assertEquals(target, list.get("Target"))
        assertEquals(target2, list.get("proxy"))
    }

    @Test
    fun toList() {
        val target = NamedString("Target")

        val list = listOf(target).toNameSearchableList()
        assertEquals(1, list.size)
        assertEquals(target, list.get("Target"))
    }

    @Test
    fun existsExactCleansString() {
        val target = NamedString("Target")

        val list = listOf(target).toNameSearchableList()
        assertTrue(list.existsExact(" TARGET "))
    }

    @Test
    fun existsExactMatchesProxy() {
        val target = NamedString("Target")

        val list = listOf(target).toNameSearchableList()
        list.addProxy(target, "Carget")
        assertTrue(list.existsExact(" carget "))
    }

    @Test
    fun existsExactDoesNotReturnPartialMatch() {
        val target = NamedString("Target")

        val list = listOf(target).toNameSearchableList()
        assertFalse(list.existsExact("tar"))
    }

    @Test
    fun removeTargetAlsoRemovesProxy() {
        val target = NamedString("Target")

        val list = NameSearchableList<NamedString>()
        list.addProxy(target, listOf("proxy"))

        val actual = list.get("proxy")
        assertEquals(target, actual)
        list.remove(target)
        assertNull(list.getOrNull(target.name))
        assertNull(list.getOrNull("proxy"))
    }

    @Test
    fun getTriesWithSpaces() {
        val target = NamedString("A Target")

        val list = NameSearchableList<NamedString>()
        list.add(target)

        val actual = list.get("ATarget")
        assertEquals(target, actual)
    }

}


