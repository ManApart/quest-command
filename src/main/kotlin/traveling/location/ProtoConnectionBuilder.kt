package traveling.location

import traveling.position.NO_VECTOR
import traveling.position.Vector

class ProtoConnectionBuilder {
    private var name: String? = null
    private var target: String? = null
    private var part: String? = null
    private var restricted = false
    private var oneWay = false
    private var vector = NO_VECTOR
    private var connectsTo: ProtoTarget? = null

    fun build(): ProtoConnection {
        return ProtoConnection(target, part, vector, name, restricted = restricted, oneWay = oneWay)
    }

    fun name(name: String) {
        this.name = name
    }

    fun target(target: String) {
        this.target = target
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

    fun vector(v: Vector) {
        this.vector = v
    }

    fun vector(x: Int = 0, y: Int = 0, z: Int = 0) {
        this.vector = Vector(x, y, z)
    }

    fun x(x: Int) {
        vector = vector.plus(Vector(x = x))
    }

    fun y(y: Int) {
        vector = vector.plus(Vector(y = y))
    }

    fun z(z: Int) {
        vector = vector.plus(Vector(z = z))
    }

    fun connectsTo(protoTarget: ProtoTarget) {
        this.connectsTo = protoTarget
    }

    fun connectsTo(location: String, network: String? = null, target: String? = null, part: String? = null) {
        this.connectsTo = ProtoTarget(location, network, target, part)
    }

}

fun connection(initializer: ProtoConnectionBuilder.() -> Unit): ProtoConnection {
    return ProtoConnectionBuilder().apply(initializer).build()
}