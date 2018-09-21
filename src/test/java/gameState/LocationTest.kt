package gameState

import core.gameState.Location
import core.utility.NameSearchableList
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertTrue

class LocationTest {

    private val innerChild = Location("inner")
    private val childOfTwoNames = Location("two child")
    private val twoNames = Location("two names", locations = NameSearchableList(childOfTwoNames))
    private val outerChild1 = Location("outer", locations = NameSearchableList(listOf(innerChild, twoNames)))
    //Command parser converts all args to lower case
    private val outerChild2 = Location("outerSolo")
    private val outSide = Location("out side")
    private val parent = Location("Parent", locations = NameSearchableList(listOf(outerChild1, outerChild2, outSide)))

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
    fun findLocationWhoseNameSharesLettersWithAnotherName(){
        val args = listOf("out", "side")
        val found = parent.findLocation(args)

        Assert.assertEquals(outSide, found)
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
    fun findLocationTwoNamesPartial(){
        val args = listOf("outer", "two")
        val found = parent.findLocation(args)

        Assert.assertEquals(twoNames, found)
    }

    @Test
    fun findLocationTwoNamesChild(){
        val args = listOf("outer", "two", "names", "two", "child")
        val found = parent.findLocation(args)

        Assert.assertEquals(childOfTwoNames, found)
    }

    @Test
    fun findLocationTwoNamesChildPartial(){
        val args = listOf("outer", "two", "names", "two")
        val found = parent.findLocation(args)

        Assert.assertEquals(childOfTwoNames, found)
    }


}