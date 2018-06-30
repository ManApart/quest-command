package gameState

object GameState {
    val player = Player()

    //TODO - parse locations
    val locations = loadLocations()



    private fun loadLocations(): List<Location> {
        val json = ""
//        return jacksonObjectMapper().readValue(json)
        return listOf()
    }
}