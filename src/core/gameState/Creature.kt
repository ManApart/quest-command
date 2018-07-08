package core.gameState

interface Creature : Target {
    val soul: Soul
    val inventory: Inventory
}