package system

import core.DependencyInjector
import core.body.*
import org.junit.Assert
import org.junit.Test

import kotlin.test.assertNotNull

class BodyManagerTest {

    @Test
    fun `NONE Body Loads`() {
        assertNotNull(NONE)
    }

    @Test
    fun bodyManagerLoadsBodies(){
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}