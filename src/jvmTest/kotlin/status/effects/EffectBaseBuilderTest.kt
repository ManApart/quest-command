package status.effects



import kotlin.test.Test
import status.stat.StatEffect
import kotlin.test.assertEquals

class EffectBaseBuilderTest{

    @Test
    fun shortenedMethodSignaturesWork(){
        val thing = EffectBaseBuilder("thing").apply {
            drain()
        }.build()

        assertEquals(StatEffect.DRAIN, thing.statEffect)
    }

    @Test
    fun nestingWorks(){
        val values = effects {
            statThing("Health"){
                effectType(StatEffect.DRAIN){
                    effect("Test Effect"){
                        description("This is a test")
                    }
                }
            }
        }
        assertEquals(1, values.size)
        val actual = values.first()
        assertEquals("Health", actual.statThing)
        assertEquals(StatEffect.DRAIN, actual.statEffect)
        assertEquals("This is a test", actual.description)

    }
}