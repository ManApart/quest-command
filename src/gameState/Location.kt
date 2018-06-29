package gameState

class Location(val name: String, val locations: List<Location> = listOf()) {
    //    private val parent: Location =

    fun findLocation(args: List<String>) : Location{
        return if (args.isEmpty()){
            this
        } else {
            val child = locations.firstOrNull {it.name.toLowerCase().trim() == args[0]}
            when {
                child == null -> this
                args.size == 1 -> child
                else -> child.findLocation(args.subList(1, args.size))
            }
        }
    }

    fun getPath() : List<String>{
        return listOf()
    }
}