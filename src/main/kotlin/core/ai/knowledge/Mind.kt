package core.ai.knowledge

import core.thing.Thing

class Mind {
    lateinit var creature: Thing

    fun knows(kind: String, relatesTo: Subject): Relationship {
        return knows(Subject(creature), relatesTo, kind)
    }

    fun knows(source: Subject, relatesTo: Subject, kind: String): Relationship {
        return Relationship(Subject(""), "", Subject(""), 0, 0)
    }

    fun knowsFact(kind: String): Fact {
        return knowsFact(Subject(creature), kind)
    }

    fun knowsFact(source: Subject, kind: String): Fact {
        return Fact(Subject(""), "", 0, 0)
    }

}