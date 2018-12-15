package core.utility

import org.junit.Test
import kotlin.test.assertEquals

class ResourceHelperTest {

    @Test
    fun getFilesInPath() {
        val files = ResourceHelper.getResourceFiles("/test/src/content")
        assertEquals(2, files.size)
        assertEquals("/test/src/content/InheritanceTest.json", files[0])
        assertEquals("/test/src/content/InheritanceTestResult.json", files[1])
    }

    @Test
    fun getFilesInPathRecursive() {
        val files = ResourceHelper.getResourceFiles("/test", true)
        assertEquals(4, files.size)
    }
}