package traveling.location

import traveling.position.VectorParent
import traveling.position.VectorParentI

class ProtoConnectionBuilder : VectorParent by VectorParentI() {
    private var name: String? = null
    private var thing: String? = null
    private var part: String? = null
    private var restricted = false
    private var oneWay = false
    private var hidden = false
    private var connectsTo: ProtoThing? = null

    fun build(): ProtoConnection {
        return ProtoConnection(thing, part, vector, name, connectsTo, restricted, oneWay, hidden)
    }

    fun name(name: String) {
        this.name = name
    }

    fun thing(thing: String) {
        this.thing = thing
    }

    fun part(part: String) {
        this.part = part
    }

    fun restricted(yes: Boolean) {
        this.restricted = yes
    }

    fun oneWay(yes: Boolean) {
        this.oneWay = yes
    }

    fun hidden(yes: Boolean) {
        this.hidden = yes
    }

    fun connectsTo(protoThing: ProtoThing) {
        this.connectsTo = protoThing
    }

    fun connectsTo(location: String, network: String? = null, thing: String? = null, part: String? = null) {
        this.connectsTo = ProtoThing(location, network, thing, part)
    }

}

fun connection(initializer: ProtoConnectionBuilder.() -> Unit): ProtoConnection {
    return ProtoConnectionBuilder().apply(initializer).build()
}