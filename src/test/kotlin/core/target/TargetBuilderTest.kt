package core.target

import org.junit.Test

class TargetBuilderTest {
    @Test
    fun basicBuild() {
        target("Bob"){
            props {
                tag("Person")
            }
        }
    }
}