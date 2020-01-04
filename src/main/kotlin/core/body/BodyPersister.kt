package core.body

    fun getPersisted(dataObject: Body): Map<String, Any> {
        val data = mutableMapOf<String, Any>("version" to 1)
        return data
    }

//    @Suppress("UNCHECKED_CAST")
//    fun readFromData(data: Map<String, Any>): Body {
//
//    }