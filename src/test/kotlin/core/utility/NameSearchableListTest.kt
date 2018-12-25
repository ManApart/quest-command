package core.utility

import org.junit.Assert.assertEquals
import org.junit.Test

class NameSearchableListTest {
    private class TestSearchable(override val name: String) : Named

    @Test
    fun targetExistsFull() {
        val target = TestSearchable("Full Match")
        val badTarget = TestSearchable("No Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists(target.name)

        assertEquals(false, baseline)
        assertEquals(true, actual)
    }

    @Test
    fun targetExistsPartial() {
        val target = TestSearchable("Partial Match")
        val badTarget = TestSearchable("No Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists("Partial")

        assertEquals(false, baseline)
        assertEquals(true, actual)
    }

    @Test
    fun targetExistsUsingListFull() {
        val target = TestSearchable("Full Match")
        val badTarget = TestSearchable("No Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists(target.name)

        assertEquals(false, baseline)
        assertEquals(true, actual)
    }

    @Test
    fun targetExistsUsingListPartial() {
        val target = TestSearchable("Partial Match")
        val badTarget = TestSearchable("No Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val baseline = list.exists(badTarget.name)
        val actual = list.exists("Partial")

        assertEquals(false, baseline)
        assertEquals(true, actual)
    }

    @Test
    fun getTargetFull() {
        val target = TestSearchable("Full Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val actual = list.get(target.name)

        assertEquals(target, actual)
    }

    @Test
    fun getTargetPartial() {
        val target = TestSearchable("Partial Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val actual = list.get("Partial")

        assertEquals(target, actual)
    }

    @Test
    fun getTargetUsingListFull() {
        val target = TestSearchable("Full Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val actual = list.get(target.name)

        assertEquals(target, actual)
    }

    @Test
    fun getTargetUsingListPartial() {
        val target = TestSearchable("Partial Match")

        val list = NameSearchableList<TestSearchable>()
        list.add(target)

        val actual = list.get("Partial")

        assertEquals(target, actual)
    }
}