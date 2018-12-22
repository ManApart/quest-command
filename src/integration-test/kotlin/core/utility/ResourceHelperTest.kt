package core.utility

import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class ResourceHelperTest {

    @Test
    fun getFilesInPath() {
        val files = ResourceHelper.getResourceFiles("/test/core/utility", false)
        assertEquals(1, files.size)
        assertEquals("/test/core/utility/MultiObjectTest.json", files[0])
    }

    @Test
    fun getFilesInPathRecursive() {
        val files = ResourceHelper.getResourceFiles("/test/core", true)
        assertEquals(3, files.size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun unableToFindPathThrowsError() {
        ResourceHelper.getResourceFiles("/test/core/does/not/exist")
    }
}