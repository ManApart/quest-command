package core.utility

import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class ResourceHelperTest {

    @Test
    fun getFilesInPath() {
        val files = ResourceHelper.getResourceFiles("/test/core/utility", false)
        assertEquals(2, files.size)
        assertEquals("/test/core/utility/BodiesTest.json", files[0])
        assertEquals("/test/core/utility/SecondBodiesTest.json", files[1])
    }

    @Test
    fun getFilesInPathRecursive() {
        val files = ResourceHelper.getResourceFiles("/test/core", true)
        assertEquals(2, files.size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun unableToFindPathThrowsError() {
        ResourceHelper.getResourceFiles("/test/core/does/not/exist")
    }
}