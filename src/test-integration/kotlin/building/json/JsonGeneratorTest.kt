package building.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class JsonGeneratorTest {

    @Test
    fun sampleConversion() {
        JsonGenerator.generate("./src/test-integration/resource", "/test/src/content", "/test/generated/content")
        val mapper = jacksonObjectMapper()

        val expected: List<MutableMap<String, Any>> = mapper.readValue(File("./src/test-integration/resource/test/results/InheritanceTestResult.json").readText())
        val actual: List<MutableMap<String, Any>> = mapper.readValue(File("./src/test-integration/resource/test/generated/content/InheritanceTest.json").readText())

        assertEquals(expected, actual)
    }

}