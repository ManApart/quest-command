package building.json

import org.junit.Test
import kotlin.test.assertEquals

class JsonConverterTest {

    @Test(expected = IllegalArgumentException::class)
    fun itemWithoutNameThrowsError() {
        val data = createConversions(listOf<MutableMap<String, Any>>(mutableMapOf("weight" to "1")))
        JsonConverter(data).transform(data.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemWithoutStringNameThrowsError() {
        val data = createConversions(listOf<MutableMap<String, Any>>(mutableMapOf("name" to 1)))
        JsonConverter(data).transform(data.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemWithNonStringExtendsThrowsError() {
        val data = createConversions(listOf(mutableMapOf("name" to "Apple", "extends" to 1)))
        JsonConverter(data).transform(data.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun itemExtendsNonExistentItemThrowsError() {
        val data = createConversions(listOf<MutableMap<String, Any>>(mutableMapOf("name" to "Apple", "extends" to "Nothing")))
        JsonConverter(data).transform(data.first())
    }

    @Test
    fun propertyInherited() {
        val data = createConversions(listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "extends" to "Apple")))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "1"))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun propertyValueOverwrittenByChild() {
        val data = createConversions(listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "2", "extends" to "Apple")))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "2"))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun nestedPropertyInherited() {
        val data = createConversions(listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "extends" to "Apple")))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun nestedPropertyMergedWhenInherited() {
        val data = createConversions(listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "properties" to mutableMapOf("tags" to mutableListOf("fruit")))))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1))),
                mutableMapOf("name" to "Pear", "properties" to mutableMapOf("values" to mutableMapOf("healAmount" to 1), "tags" to mutableListOf("fruit"))))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun stringArrayInherited() {
        val data = createConversions(listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "extends" to "Apple")))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "tags" to listOf("1", "2")))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun existingStringArrayAppended() {
        val data = createConversions(listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "tags" to listOf("3"))))

        val expected = listOf(
                mutableMapOf("name" to "Apple", "tags" to listOf("1", "2")),
                mutableMapOf("name" to "Pear", "tags" to listOf("1", "2", "3")))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }

    @Test
    fun extensionIsRecursive() {
        val data = createConversions(listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "extends" to "Apple", "value" to "1"),
                mutableMapOf("name" to "Orange", "extends" to "Pear", "color" to "Orange")))

        val expected = listOf<MutableMap<String, Any>>(
                mutableMapOf("name" to "Apple", "weight" to "1"),
                mutableMapOf("name" to "Pear", "weight" to "1", "value" to "1"),
                mutableMapOf("name" to "Orange", "weight" to "1", "value" to "1", "color" to "Orange"))

        assertEquals(expected, JsonConverter(data).transform(data.first()))
    }


    private fun createConversion(data: List<MutableMap<String, Any>>): JsonFileConversion {
        return JsonFileConversion("inputPath", "outputPath", data)
    }

    private fun createConversions(data: List<MutableMap<String, Any>>): List<JsonFileConversion> {
        return listOf(JsonFileConversion("inputPath", "outputPath", data))
    }
}