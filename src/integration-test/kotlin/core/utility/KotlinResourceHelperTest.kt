package core.utility

import org.junit.Test
import kotlin.test.assertEquals

class KotlinResourceHelperTest {

    @Test
    fun getFilesInPath() {
        val files = KotlinResourceHelper().getResourceFiles("/test/core/utility", false)
        assertEquals(1, files.size)
        assertEquals("/test/core/utility/MultiObjectTest.json", files[0])
    }

    @Test
    fun getFilesInPathRecursive() {
        val files = KotlinResourceHelper().getResourceFiles("/test/core", true)
        assertEquals(3, files.size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun unableToFindPathThrowsError() {
        KotlinResourceHelper().getResourceFiles("/test/core/does/not/exist")
    }
}