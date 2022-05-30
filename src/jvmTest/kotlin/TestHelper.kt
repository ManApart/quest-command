import core.utility.Named
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun assertEqualsByName(expected: Named, actual: Named) {
    assertEquals(expected.name, actual.name)
}

fun assertContainsByName(list: Collection<Named>, expected: Named) {
    val names = list.map { it.name }
    assertTrue(names.contains(expected.name), "${expected.name} was not found in $names")
}
