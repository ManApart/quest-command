package building.json

import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class JsonConverterTest {

    @Test(expected = IllegalArgumentException::class)
    fun itemWithoutNameThrowsError() {
        val data = listOf<MutableMap<String, Any>>(mutableMapOf("weight" to "1"))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemWithoutStringNameThrowsError() {
        val data = listOf<MutableMap<String, Any>>(mutableMapOf("name" to 1))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemWithNonStringExtendsThrowsError() {
        val data = listOf(mutableMapOf("name" to "Apple", "extends" to 1))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemExtendsNonExistentItemThrowsError() {
        val data = listOf<MutableMap<String, Any>>(mutableMapOf("name" to "Apple", "extends" to "Nothing"))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun baseItemWithUnsatisfiedVariableKeyThrowsError() {
        val data = listOf<MutableMap<String, Any>>(mutableMapOf("name" to "Apple", "\$variable" to "variable"))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun baseItemWithUnsatisfiedVariableValueThrowsError() {
        val data = listOf<MutableMap<String, Any>>(mutableMapOf("name" to "Apple", "variable" to "\$variable"))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun baseItemWithUnsatisfiedNestedVariableThrowsError() {
        val data = listOf(mutableMapOf("name" to "Apple", "topic" to mutableMapOf("variable" to "\$variable")))
        JsonConverter(data).transform()
    }

    @Test(expected = IllegalArgumentException::class)
    fun extendedItemWithUnsatisfiedVariablesThrowsError() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple"),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "topic" to mutableMapOf("variable" to "\$variable"))
        )
        JsonConverter(data).transform()
    }

    @Test
    fun propertyInherited() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "extends" to "Apple"))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "1"))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun propertyValueOverwrittenByChild() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "2", "extends" to "Apple"))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "2"))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun nestedPropertyInherited() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "extends" to "Apple"))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun nestedPropertyMergedWhenInherited() {
        val data = listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "properties" to mutableMapOf("tags" to mutableListOf("fruit"))))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1), "tags" to mutableListOf("fruit"))))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun stringArrayInherited() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "extends" to "Apple"))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "tags" to listOf("1", "2")))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun existingStringArrayAppended() {
        val data = listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "tags" to listOf("3")))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "tags" to listOf("1", "2", "3")))

        assertEquals(expected, JsonConverter(data).transform())
    }

    @Test
    fun extensionIsRecursive() {
        val data = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "value" to "1"),
                mutableMapOf("name" to "Orange", "extends" to "Pear", "color" to "Orange"))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "1", "value" to "1"),
                mutableMapOf("name" to "Orange", "weight" to "1", "value" to "1", "color" to "Orange"))

        assertEquals(expected, JsonConverter(data).transform())
    }
}