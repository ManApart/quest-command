package traveling.location

import traveling.position.NO_VECTOR
import traveling.position.Vector

class ProtoConnectionBuilder {
    private var name: String? = null
    private var thing: String? = null
    private var part: String? = null
    private var restricted = false
    private var oneWay = false
    private var hidden = false
    private var connectsTo: ProtoThing? = null
    private var originPoint = NO_VECTOR
    private var destinationPoint = NO_VECTOR

    fun build(): ProtoConnection {
        return ProtoConnection(thing, part, originPoint, destinationPoint, name, connectsTo, restricted, oneWay, hidden)
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

    fun origin(v: Vector) {
        this.originPoint = v
    }

    fun origin(x: Int = 0, y: Int=0, z: Int=0) {
        this.originPoint = Vector(x, y, z)
    }

    fun x(x: Int) {
        originPoint = originPoint.plus(Vector(x = x))
    }

    fun y(y: Int) {
        originPoint = originPoint.plus(Vector(y = y))
    }

    fun z(z: Int) {
        originPoint = originPoint.plus(Vector(z = z))
    }

    fun dest(v: Vector) {
        this.destinationPoint = v
    }

    fun dest(x: Int, y: Int, z: Int) {
        this.destinationPoint = Vector(x, y, z)
    }

    fun dest(x: Int) {
        destinationPoint = destinationPoint.plus(Vector(x = x))
    }

    fun destY(y: Int) {
        destinationPoint = destinationPoint.plus(Vector(y = y))
    }

    fun destZ(z: Int) {
        destinationPoint = destinationPoint.plus(Vector(z = z))
    }

}

fun connection(initializer: ProtoConnectionBuilder.() -> Unit): ProtoConnection {
    return ProtoConnectionBuilder().apply(initializer).build()
}