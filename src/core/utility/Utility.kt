package core.utility

fun <A, B> mapsMatch(map: Map<A, B>, other: Map<A, B>) : Boolean {
    map.keys.forEach {
        if (!other.containsKey(it)) {
            return false
        } else if (map[it] != other[it]){
            return false
        }
    }
    other.keys.forEach {
        if (!map.containsKey(it)) {
            return false
        } else if (map[it] != other[it]){
            return false
        }
    }
    return true
}