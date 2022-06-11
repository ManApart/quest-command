package system

import core.DependencyInjector
import core.body.*
import kotlin.test.Test


import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

        assertTrue(BodyManager.bodyExists("Human"))
    }

}