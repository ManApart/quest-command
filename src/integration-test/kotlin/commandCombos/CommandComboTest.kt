package commandCombos

import core.commands.CommandParser
import core.gameState.GameState
import core.gameState.quests.QuestManager
import core.history.ChatHistory
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.EventManager
import system.GameManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommandComboTest {

    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame()
        EventManager.executeEvents()
    }

    @Test
    fun sliceApple() {
        val input = "use dagger on apple"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
    }

    @Test
    fun roastApple() {
        val input = "w && s && pickup tinder box && n && e && n && use tinder on tree && use apple on tree"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Roasted") ?: false)
    }

    @Test
    fun cookApple() {
        val stat = GameState.player.soul.getStats().first { it.name.toLowerCase() == "cooking" }
        stat.setLevel(2)

        val input = "w && s && cook apple on range"
        CommandParser.parseCommand(input)
        assertTrue(GameState.player.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.inventory.getItem("Apple")?.properties?.tags?.has("Cooked") ?: false)
    }

    @Test
    fun chopTree() {
        val input = "n && pickup hatchet && equip hatchet && y && chop tree"
        CommandParser.parseCommand(input)
        assertEquals("The Dulled Hatchet hacks at Apple Tree.", ChatHistory.getLastOutput())
    }

    @Test
    fun climbTree() {
        //TODO - somehow remove random element and test full climb
        val input = "n && climb tree"
        CommandParser.parseCommand(input)
        assertEquals("Above you are path choices 1, 2. You are at the bottom of Apple Tree.", ChatHistory.getLastOutput())
    }

    @Test
    fun fightRat() {
        CommandParser.parseCommand("s")
        val input = "slash bottom center of rat && r && r"
        CommandParser.parseCommand(input)
        assertEquals("The battle ends.", ChatHistory.getLastOutput())
    }

    @Test
    fun dontAttackDeadThing() {
        val input = "s && slash bottom center of rat && slash bottom center of rat && slash bottom center of rat && slash bottom center of rat"
        CommandParser.parseCommand(input)
        assertEquals("Couldn't find rat.", ChatHistory.getLastOutput())
    }

    @Test
    fun useGate() {
        val input = "w && w && use gate"
        CommandParser.parseCommand(input)
        assertEquals("You can now access Kanbara City from Kanbara Gate.", ChatHistory.getLastOutput())
    }

    @Test
    fun millFlour() {
        val input = "slash wheat && pickup wheat && ne && a && a && place wheat in chute && d && d && take wheat from bin"
        CommandParser.parseCommand(input)
        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
    }

    @Test
    fun makePie() {
        val input = "slash wheat && pickup wheat && t hut && take bucket && use bucket on well && t windmill && t" +
                "&& a && a && place wheat in chute && d && d && take wheat from bin && use flour on bucket" +
                "&& use dagger on apple" +
                "&& t interior && t && t && take pie tin" +
                "&& read recipe && rest" +
                "&& take box && use box on range && craft apple pie"
        CommandParser.parseCommand(input)

        assertNotNull(GameState.player.inventory.getItem("Apple Pie"))
        assertEquals(true, QuestManager.quests.get("A Simple Pie").complete)

    }

}