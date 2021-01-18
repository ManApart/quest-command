package core.reflection

import core.events.eventParsers.EventParser

interface EventParsersCollection {
    val values: List<EventParser>
}