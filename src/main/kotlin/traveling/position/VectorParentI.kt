package traveling.position

class VectorParentI : VectorParent {
    override val vector: Vector
        get() = vectorInternal

    private var vectorInternal = NO_VECTOR

    override fun vector(v: Vector) {
        this.vectorInternal = v
    }

    override fun vector(x: Int, y: Int, z: Int) {
        this.vectorInternal = Vector(x, y, z)
    }

    override fun x(x: Int) {
        vectorInternal = vector.plus(Vector(x = x))
    }

    override fun y(y: Int) {
        vectorInternal = vector.plus(Vector(y = y))
    }

    override fun z(z: Int) {
        vectorInternal = vector.plus(Vector(z = z))
    }
}