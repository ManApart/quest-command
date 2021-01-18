package core.utility.reflection

import core.events.EventListener
import core.reflection.EventListenersCollection

class MockEventListeners(override val values: List<EventListener<*>> = listOf()) : EventListenersCollection