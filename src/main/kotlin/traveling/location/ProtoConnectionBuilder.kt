package traveling.location

import traveling.position.VectorParent
import traveling.position.VectorParentI

class ProtoConnectionBuilder : VectorParent by VectorParentI() {
    private var name: String? = null
    private var target: String? = null
    private var part: String? = null
    private var restricted = false
    private var oneWay = false
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