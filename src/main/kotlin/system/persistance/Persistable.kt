package system.persistance

interface Persistable<E> {

    fun getVersion(): Int
    fun getPersisted(dataObject: E) : Map<String, Any>
    fun applyData(data: Map<String, Any>) : E
}