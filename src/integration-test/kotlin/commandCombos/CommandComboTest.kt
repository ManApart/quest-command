package commandCombos

import core.commands.CommandParser
import core.gameState.GameState
import core.gameState.Player
import core.gameState.quests.QuestManager
import core.history.ChatHistory
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.EventManager
import system.GameManager
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
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
    fun eatApple() {
        val input = "eat apple"
        CommandParser.parseCommand(input)
        assertNull(GameState.player.inventory.getItem("Apple"))
        assertEquals("You feel the fullness of life beating in your bosom.", ChatHistory.getLastOutput())
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
        val input = "n && pickup hatchet && equip hatchet && y && ch tree"
        CommandParser.parseCommand(input)
        assertEquals("Dulled Hatchet decreases Apple Tree's chopHealth from 5 to 1.", ChatHistory.getLastOutput())
    }

    @Test
    fun climbTree() {
        //TODO - somehow remove random element and test full climb
        val input = "n && climb tree && climb && d && d"
        CommandParser.parseCommand(input)
        assertEquals("You Climb to Apple Tree Branches. It is neighbored by Apple Tree (BELOW).", ChatHistory.history[1].outPut[8])
        assertEquals("You climb back off Apple Tree.", ChatHistory.getLastOutput())
    }

    @Test
    fun fightRat() {
        CommandParser.parseCommand("s")
        val input = "slash body of rat && r && r"
        CommandParser.parseCommand(input)
        assertEquals("The battle ends.", ChatHistory.getLastOutput())
    }

    @Test
    fun dontAttackDeadThing() {
        val input = "s && slash body of rat && sl rat && sl && slash rat"
        CommandParser.parseCommand(input)
        val expected = "slash what with Rusty Dagger?\n\tPlayer, Poor Quality Meat"
        assertEquals(expected, ChatHistory.getLastOutput())
    }

    @Test
    fun useGate() {
        val input = "w && w && use gate"
        CommandParser.parseCommand(input)
        assertEquals("You can now access Kanbara City from Kanbara Gate.", ChatHistory.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughGate() {
        val input = "w && w && use gate && w"
        CommandParser.parseCommand(input)
        assertEquals("You travel to Kanbara City. It is neighbored by Kanbara Gate (EAST), Kanbara Pub, Mapmaker Manor, Kanbara City South, Kanbara Wall North (SOUTH).", ChatHistory.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughWall() {
        val input = "w && w && sw && c && c && c && c && d && d && d && ls"
        CommandParser.parseCommand(input)
        assertEquals("You are at Kanbara City South", ChatHistory.getCurrent().outPut[3])
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
                "&& read recipe && wait" +
                "&& take box && use box on range && craft apple pie"
        CommandParser.parseCommand(input)

        assertNotNull(GameState.player.inventory.getItem("Apple Pie"))
        assertEquals(true, QuestManager.quests.get("A Simple Pie").complete)

    }

    @Test
    fun viewMapByAliasIgnoringResponseRequest() {
        val input = "commands && m"
        CommandParser.parseCommand(input)

        val expected = """
            An Open Field is a part of Kanbara Countryside. It is neighbored by:
              Name             Distance  Direction Path  
              Farmer's Hut     1         W               
              Apple Tree       1         N               
              Barren Patch     1         S               
              Training Circle  1         E               
              Windmill         1         NE
              """.trimIndent().trim()

        assertEquals(expected, ChatHistory.getLastOutput().trimIndent().trim())
    }

}