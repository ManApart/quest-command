package core.gameState

interface Creature : Target {
    val soul: Soul
    val inventory: Inventory
//    val location: Location
    //val body: Body
    //TODO - make this a class instead of an interface
}