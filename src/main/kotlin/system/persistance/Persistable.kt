package system.persistance

interface Persistable {

    fun getVersion(): Int
    fun getPersisted() : Map<String, Any>
    fun applyData(data: Map<String, Any>)
}