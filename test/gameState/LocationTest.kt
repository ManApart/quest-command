package gameState

import core.gameState.Location
import org.junit.Assert
import org.junit.Test

class LocationTest {

    private val innerChild = Location("inner")
    private val twoNames = Location("two names")
    private val childOfTwoNames = Location("child")
    private val outerChild1 = Location("outer", locations = listOf(innerChild, twoNames))
    //Command parser converts all args to lower case
    private val outerChild2 = Location("outerSolo")
    private val parent = Location("Parent", locations = listOf(outerChild1, outerChild2))

    @Test
    fun findLocationParent(){
        val args = listOf<String>()
        val found = parent.findLocation(args)

        Assert.assertEquals(parent, found)
    }

    @Test
    fun findLocationOuter(){
        val args = listOf("outerSolo")
        val found = parent.findLocation(args)

        Assert.assertEquals(outerChild2, found)
    }

    @Test
    fun findLocationInner(){
        val args = listOf("outer", "inner")
        val found = parent.findLocation(args)

        Assert.assertEquals(innerChild, found)
    }

    @Test
    fun findLocationTwoNames(){
        val args = listOf("outer", "two", "names")
        val found = parent.findLocation(args)

        Assert.assertEquals(twoNames, found)
    }

    @Test
    fun findLocationTwoNamesChild(){
        val args = listOf("outer", "two", "names", "child")
        val found = parent.findLocation(args)

        Assert.assertEquals(twoNames, found)
    }


}