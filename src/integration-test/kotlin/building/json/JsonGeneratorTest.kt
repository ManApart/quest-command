package building.json

import org.junit.Test

class JsonGeneratorTest {

    @Test
    fun doThing() {
        JsonGenerator.generate("./src/integration-test/resource", "/test/src/content", "/test/generated/content")
    }
}