package traveling.position

interface VectorParent {
    val vector: Vector
//    fun getVector(): Vector
    fun vector(v: Vector)
    fun vector(x: Int = 0, y: Int = 0, z: Int = 0)
    fun x(x: Int)
    fun y(y: Int)
    fun z(z: Int)
}