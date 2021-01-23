package quests

import org.junit.Test
import use.interaction.InteractEvent
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StoryEventTest {

    @Test
    fun canBeListenedForExactStage() {
        val event = createEvent(5)
        assertTrue(event.canBeListenedFor(5))
    }

    @Test
    fun canNotBeListenedForExactStage() {
        val event = createEvent(5)
        assertFalse(event.canBeListenedFor(4))
    }

    @Test
    fun canNotBeListenedForExactStageAbove() {
        val event = createEvent(5)
        assertFalse(event.canBeListenedFor(6))
    }

    @Test
    fun canNotBeListenedForCompleted() {
        val event = createEvent(5)
        event.completed = true
        assertFalse(event.canBeListenedFor(5))
    }

    @Test
    fun canBeListenedForCompletedRepeatable() {
        val event = createEvent(5, repeatable = true)
        event.completed = true
        assertTrue(event.canBeListenedFor(5))
    }

    @Test
    fun canBeListenedForAvailableAfter() {
        val event = createEvent(10, availableAfter = 5) //before defaults to 10 in this case
        assertTrue(event.canBeListenedFor(7))
    }

    @Test
    fun canBeListenedForAvailableBefore() {
        val event = createEvent(5, availableBefore = 20) //after defaults to 10 in this case
        assertTrue(event.canBeListenedFor(15))
    }

    private fun createEvent(previousStage: Int, availableBefore: Int = -1, availableAfter: Int = -1, repeatable: Boolean = false): StoryEvent {
        val event = StoryEvent("quest", 100, "journal", ConditionalEvents(InteractEvent::class.java), repeatable, availableAfter, availableBefore)
        event.setDefaultAvailability(previousStage)
        return event
    }
}