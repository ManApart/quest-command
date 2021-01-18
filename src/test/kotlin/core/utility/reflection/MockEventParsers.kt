package core.utility.reflection

import core.events.eventParsers.EventParser
import core.reflection.EventParsersCollection

class MockEventParsers(override val values: List<EventParser> = listOf()) : EventParsersCollection