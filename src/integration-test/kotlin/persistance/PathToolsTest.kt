package persistance

import org.junit.Test
import system.persistance.clean
import system.persistance.cleanPathToFile
import kotlin.test.assertEquals

class PathToolsTest {

    @Test
    fun dirtyPathIsCleaned(){
        val cleanPath = "./this/path/is/dirty/"

        assertEquals(cleanPath, clean("./this//" +"path/is//dirty"))
    }

    @Test
    fun cleanRemovesMultiplePeriods(){
        val dirtyPath = "./this//path/is//dirty..json"
        val cleanPath = "./this/path/is/dirty.json/"

        assertEquals(cleanPath, clean(dirtyPath))
    }

    @Test
    fun dirtyPathToFileAddsExtensionPeriod(){
        val dirtyPath = "./this//path/is//dirty"
        val cleanPath = "./this/path/is/dirty.json"

        assertEquals(cleanPath, cleanPathToFile("json", dirtyPath))
    }


}