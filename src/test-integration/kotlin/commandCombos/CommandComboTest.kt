package commandCombos

import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import org.junit.Before
import org.junit.Test
import quests.QuestManager
import status.stat.HEALTH
import system.debug.DebugType
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CommandComboTest {

    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame(testing = true)
        EventManager.executeEvents()
    }

    @Test
    fun sliceApple() {
        val input = "use dagger on apple"
        CommandParsers.parseCommand(GameState.player, input)
        assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
    }

    @Test
    fun chopApple() {
        val input = "chop apple"
        CommandParsers.parseCommand(GameState.player, input)
        assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
    }

    @Test
    fun eatApple() {
        val input = "sl head of self && eat apple && n && eat apple"
        CommandParsers.parseCommand(GameState.player, input)
        assertNull(GameState.player.thing.inventory.getItem("Apple"))
        assertEquals("You feel the fullness of life beating in your bosom.", GameLogger.main.getLastOutput())
    }

    @Test
    fun roastApple() {
        val input = "w && s && rs 10 && move to range && pickup tinder box && n && e && n && rs 10 && use tinder on tree && use apple on tree"
        CommandParsers.parseCommand(GameState.player, input)
        assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Roasted") ?: false)
    }

    @Test
    fun cookApple() {
        val stat = GameState.player.thing.soul.getStats().first { it.name.lowercase() == "cooking" }
        stat.setLevel(2)

        val input = "w && s && move to range && cook apple on range"
        CommandParsers.parseCommand(GameState.player, input)
        assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
        assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Cooked") ?: false)
    }

    @Test
    fun makeFire() {
        val input = "n && pickup hatchet && ch tree && ch tree"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals(0, GameState.player.thing.currentLocation().getActivators("tree").size)
        assertEquals(1, GameState.player.thing.currentLocation().getActivators("logs").size)
        CommandParsers.parseCommand(GameState.player, "cast flame 1 on logs")
        assertTrue(GameState.player.thing.currentLocation().getActivators("logs").first().soul.hasEffect("On Fire"))
        CommandParsers.parseCommand(GameState.player, "eat apple && eat apple && cast flame 1 on logs && rest 3")
        
        assertEquals(0, GameState.player.thing.currentLocation().getActivators("logs").size)
        assertEquals(1, GameState.player.thing.currentLocation().getItems("ash").size)
    }

    @Test
    fun climbTree() {
        val input = "db random && n && climb tree && climb && d && d"
        CommandParsers.parseCommand(GameState.player, input)
        assertTrue(GameLogger.main.history[1].outPut.contains("You climb to Apple Tree Branches. It is neighbored by Apple Tree (BELOW)."))
        assertTrue(GameLogger.main.getLastOutputs().contains("You climb back off Apple Tree."))
    }

    @Test
    fun fightRat() {
        CommandParsers.parseCommand(GameState.player, "s")
        val input = "slash body of rat && r && r"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("Rat has died.", GameLogger.main.getLastOutput())
        CommandParsers.parseCommand(GameState.player, "ex")
        assertEquals("You find yourself surrounded by Poor Quality Meat.", GameLogger.main.getLastOutput())
    }

    @Test
    fun ratFightsBack() {
        GameState.putDebug(DebugType.RANDOM_SUCCEED, true)
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        CommandParsers.parseCommand(GameState.player, "s && nothing && nothing && nothing && nothing && nothing")
GameLogger.main.getLastInput()
        assertEquals("Oh dear, you have died!", GameLogger.main.getLastOutput())
    }

    @Test
    fun doNotAttackDeadThing() {
        val input = "s && slash body of rat && sl rat && sl && slash rat"
        CommandParsers.parseCommand(GameState.player, input)
        val expected = "slash what with Rusty Dagger?\n\tPlayer, Poor Quality Meat"
        assertEquals(expected, GameLogger.main.getLastOutput())
    }

    @Test
    fun useGate() {
        val input = "w && w && use gate"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("You can now access Kanbara City from Kanbara Gate.", GameLogger.main.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughGate() {
        val input = "w && w && rs 10 && use gate && w"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("You travel to Kanbara City. It is neighbored by Kanbara Gate (EAST), Kanbara Pub, Kanbara Manor (NORTH_WEST), Kanbara City South (SOUTH_WEST), Kanbara Wall North (SOUTH).", GameLogger.main.getLastOutput())
    }

    @Test
    fun enterKanbaraThroughWall() {
        val input = "w && w && rs 10 && sw && rs 10 && cl && cl && cl && cl && d && d && d && ls"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("You are at Kanbara City South", GameLogger.main.getCurrent().outPut[3])
    }

    @Test
    fun compassToPub() {
        CommandParsers.parseCommand(GameState.player, "co pub && w && co pub")
        assertEquals("Farmer's Hut", GameState.player.thing.location.name)
        assertEquals("Kanbara Pub is WEST of you.", GameLogger.main.getLastOutput())

        CommandParsers.parseCommand(GameState.player, "co pub && w && rest 10 && co pub")
        assertEquals("Kanbara Gate", GameState.player.thing.location.name)
        assertEquals("Kanbara Pub is WEST of you.", GameLogger.main.getLastOutput())

        CommandParsers.parseCommand(GameState.player, "use gate && w && co pub")
        assertEquals("Kanbara City", GameState.player.thing.location.name)
        assertEquals("Kanbara Pub is near you.", GameLogger.main.getLastOutput())

        CommandParsers.parseCommand(GameState.player, "t pub && co pub")
        assertEquals("Kanbara Pub", GameState.player.thing.location.name)
        assertEquals("You are at Kanbara Pub.", GameLogger.main.getLastOutput())
    }

    @Test
    fun millFlour() {
        val input = "move to wheat && slash wheat && pickup wheat && ne && a && a && put wheat in chute && d && d && take wheat from bin"
        CommandParsers.parseCommand(GameState.player, input)
        assertEquals("Player picked up Wheat Flour.", GameLogger.main.getLastOutput())
    }

    @Test
    fun makePie() {
        val input = "move to wheat && slash wheat && pickup wheat && t hut && take bucket && use bucket on well && t windmill && t" +
                "&& a && a && put wheat in chute && d && d && take wheat from bin && use flour on bucket" +
                "&& use dagger on apple" +
                "&& t interior && t && t && rest 10 && move to range && take pie tin" +
                "&& read recipe && rs" +
                "&& take box && use box on range && craft apple pie"
        CommandParsers.parseCommand(GameState.player, input)

        assertNotNull(GameState.player.thing.inventory.getItem("Apple Pie"))
        assertEquals(true, QuestManager.quests.get("A Simple Pie").complete)
    }

    @Test
    fun tutorial() {
        val input = "help commands && journal && m && w && s && ls && mv 0,10,0 && read recipe "
        CommandParsers.parseCommand(GameState.player, input)

        assertEquals(true, QuestManager.quests.get("Tutorial").complete)
    }

    @Test
    fun viewMapByAliasIgnoringResponseRequest() {
        val input = "commands && m"
        CommandParsers.parseCommand(GameState.player, input)

        val expected = """
            An Open Field is a part of Kanbara Countryside. It is neighbored by:
              Name             Distance  Direction Path  
              Farmer's Hut     100       W               
              Apple Tree       100       N               
              Barren Patch     100       S               
              Training Circle  100       E               
              Windmill         180       NE
              """.trimIndent().trim()

        assertEquals(expected, GameLogger.main.getLastOutput().trimIndent().trim())
    }

    @Test
    fun poisonSelf() {
        CommandParsers.parseCommand(GameState.player, "poison 1 for 5 on head of self")
        assertEquals(1, GameState.player.thing.soul.getConditions().size)
        assertEquals("Poison decreases Your Health by 1 (9/10).", GameLogger.main.getLastOutput())

        CommandParsers.parseCommand(GameState.player, "wait 5")
        CommandParsers.parseCommand(GameState.player, "wait 1")
        assertEquals(0, GameState.player.thing.soul.getConditions().size)
        assertEquals(5, GameState.player.thing.soul.getCurrent(HEALTH))
        assertEquals("Player is no longer Poisoned.", GameLogger.main.getLastOutput())
    }

    @Test
    fun feelTheRain() {
        CommandParsers.parseCommand(GameState.player, "rest 1 && debug weather gentle rain && rest 1")
        assertEquals(1, GameState.player.thing.soul.getConditions().size)
        assertEquals("Rain Wet", GameState.player.thing.soul.getConditions().first().name)
    }

    @Test
    fun lightTheWay() {
        CommandParsers.parseCommand(GameState.player, "w && s && take tinder && n && rest 10 && e && s && s && take lantern && t mouth && ls")
        assertEquals("It's too dark to see anything.", GameLogger.main.getLastOutput())
        CommandParsers.parseCommand(GameState.player, "use tinder on lantern && ls")
        assertEquals("You find yourself surrounded by Wall Crack.", GameLogger.main.getLastOutput())
    }
}