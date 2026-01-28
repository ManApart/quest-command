package commandCombos

import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.GameLogger
import kotlinx.coroutines.runBlocking
import quests.QuestManager
import status.stat.Attributes.HEALTH
import status.stat.Skills.COOKING
import system.debug.DebugType
import kotlin.test.*

class CommandComboTest {

    @BeforeTest
    fun reset() {
        runBlocking {
            EventManager.clear()
            GameManager.newGame(testing = true)
            runBlocking { EventManager.processEvents() }
        }
    }

    @Test
    fun sliceApple() {
        runBlocking {
            val input = "use dagger on apple"
            CommandParsers.parseCommand(GameState.player, input)
            assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
            assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
        }
    }

    @Test
    fun chopApple() {
        runBlocking {
            val input = "chop apple"
            CommandParsers.parseCommand(GameState.player, input)
            assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
            assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Sliced") ?: false)
        }
    }

    @Test
    fun eatApple() {
        runBlocking {
            val input = "sl head of self && eat apple && n && eat apple"
            CommandParsers.parseCommand(GameState.player, input)
            assertNull(GameState.player.thing.inventory.getItem("Apple"))
            assertEquals(
                "You feel the fullness of life beating in your bosom.",
                GameLogger.getMainHistory().getLastOutput()
            )
        }
    }

    @Test
    fun roastApple() {
        runBlocking {
            val input =
                "w && s && rs 10 && move to range && pickup tinder box && n && e && n && rs 10 && n && use tinder on tree && use apple on tree"
            CommandParsers.parseCommand(GameState.player, input)
            assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
            assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Roasted") ?: false)
        }
    }

    @Test
    fun cookApple() {
        runBlocking {
            val stat = GameState.player.thing.soul.getStats().first { it.name.lowercase() == COOKING.lowercase() }
            stat.setLevel(2)

            val input = "w && s && move to range && cook apple on range"
            CommandParsers.parseCommand(GameState.player, input)
            assertTrue(GameState.player.thing.inventory.getItem("Apple") != null)
            assertTrue(GameState.player.thing.inventory.getItem("Apple")?.properties?.tags?.has("Cooked") ?: false)
        }
    }

    @Test
    fun makeFire() {
        runBlocking {
            val input = "n && pickup hatchet && equip hatchet to right hand grip f && ch tree && ch tree"
            CommandParsers.parseCommand(GameState.player, input)
            assertEquals(0, GameState.player.thing.currentLocation().getActivators("tree").size)
            assertEquals(1, GameState.player.thing.currentLocation().getActivators("logs").size)

            CommandParsers.parseCommand(GameState.player, "cast flame 1 on logs")
            assertTrue(GameState.player.thing.currentLocation().getActivators("logs").first().soul.hasEffect("On Fire"))

            CommandParsers.parseCommand(GameState.player, "eat apple && eat apple && cast flame 1 on logs && rest 3")
            assertEquals(0, GameState.player.thing.currentLocation().getActivators("logs").size)
            assertEquals(1, GameState.player.thing.currentLocation().getItems("ash").size)
        }
    }

    @Test
    fun climbTree() {
        val input = "db random && n && climb tree && climb && d && d"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        assertTrue(GameLogger.getMainHistory().contains("You climb to Apple Tree Branches."))
        assertTrue(GameLogger.getMainHistory().contains("It is neighbored by Apple Tree (BELOW)."))
        assertTrue(GameLogger.getMainHistory().contains("You climb back off Apple Tree."))
    }

    @Test
    fun fightRat() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "s")
            assertEquals(2, GameState.player.location.getLocation().getCreatures().size)
            val input = "slash torso of rat && r && r"
            CommandParsers.parseCommand(GameState.player, input)
            assertEquals(1, GameState.player.location.getLocation().getCreatures().size)
            CommandParsers.parseCommand(GameState.player, "ex")
            assertEquals(1, GameState.player.location.getLocation().getItems("Poor Quality Meat").size)
        }
    }

    @Test
    fun ratFightsBack() {
        GameState.putDebug(DebugType.RANDOM_SUCCEED, true)
        GameState.putDebug(DebugType.RANDOM_RESPONSE, 0)
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "s && nothing && nothing && nothing && nothing && nothing && nothing && r 1")
        }
        assertTrue(GameLogger.getMainHistory().contains("Oh dear, you have died!"))
    }

    @Test
    fun doNotAttackDeadThing() {
        val input = "s && slash torso of rat && sl rat && sl && slash rat"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        val expected = "Slash what with Rusty Dagger?"
        assertEquals(expected, GameLogger.getMainHistory().getLastOutput())
        val expectedOptions = "Old Shirt, Brown Pants, Small Pouch, Rusty Dagger, Player, Poor Quality Meat"
        val options = CommandParsers.getParser(GameState.player).getResponseRequest()?.getOptions()?.joinToString(", ")
        assertEquals(expectedOptions, options)
    }

    private val travelToGate = "w && n && sw && rest 10 && w && rs 10 && w"

    @Test
    fun useGate() {
        val input = "$travelToGate && use gate"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        assertEquals("Kanbara City is now available from Kanbara Gate.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun enterKanbaraThroughGate() {
        val input = "$travelToGate && use gate && w"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        assertTrue(GameLogger.getMainHistory().contains("You travel to Kanbara City."))
        assertTrue(
            GameLogger.getMainHistory()
                .contains("It is neighbored by Kanbara Gate (EAST), Kanbara Pub, Kanbara Manor (NORTH_WEST), Kanbara City South (SOUTH_WEST), Kanbara Wall North (SOUTH), Dwarven Tear River East (NORTH_WEST), Dwarven Tear River West (SOUTH_EAST).")
        )
    }

    @Test
    fun enterKanbaraThroughWall() {
        val input =
            "w && n && sw && rest 10 && w && rs 10 && w && sw && rs 10 && mv to wall && cl && cl && cl && cl && d && d && d && ls"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        assertTrue(GameLogger.getMainHistory().contains("You are at Kanbara City South."))
    }

    @Test
    fun compassToPub() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "co pub && w && n && co pub")
            assertEquals("Kentle", GameState.player.thing.location.name)
            assertEquals("Kanbara Pub is SOUTH_WEST of you.", GameLogger.getMainHistory().getLastOutput())

            CommandParsers.parseCommand(GameState.player, "rs 10 && sw && rs 10 && w && rest 10 && w && co pub")
            assertEquals("Kanbara Gate", GameState.player.thing.location.name)
            assertEquals("Kanbara Pub is WEST of you.", GameLogger.getMainHistory().getLastOutput())

            CommandParsers.parseCommand(GameState.player, "use gate && w && co pub")
            assertEquals("Kanbara City", GameState.player.thing.location.name)
            assertEquals("Kanbara Pub is near you.", GameLogger.getMainHistory().getLastOutput())

            CommandParsers.parseCommand(GameState.player, "t pub && co pub")
            assertEquals("Kanbara Pub", GameState.player.thing.location.name)
            assertEquals("You are at Kanbara Pub.", GameLogger.getMainHistory().getLastOutput())
        }
    }

    @Test
    fun millFlour() {
        val input =
            "move to wheat && slash wheat && pickup wheat && ne && a && a && put wheat in chute && d && d && take wheat from bin"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }
        assertEquals("You picked up Wheat Flour.", GameLogger.getMainHistory().getLastOutput())
    }

    @Test
    fun makePie() {
        runBlocking {
            val input =
                "move to wheat && slash wheat && pickup wheat && t farmer's hut && take bucket && use bucket on well && t windmill && t" +
                        "&& a && a && put wheat in chute && d && d && take wheat from bin && use flour on bucket" +
                        "&& use dagger on apple && rs 10" +
                        "&& t interior && t && t && rest 10 && move to range && take pie tin" +
                        "&& read recipe && rs" +
                        "&& take box && use box on range && craft apple pie"
            CommandParsers.parseCommand(GameState.player, input)

            assertNotNull(GameState.player.thing.inventory.getItem("Apple Pie"))
            assertEquals(true, QuestManager.quests.get("A Simple Pie").complete)
        }
    }

    @Test
    fun tutorial() {
        val input = "help commands && journal && m && w && s && ls && mv 0,10,0 && read recipe "
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }

        assertEquals(true, QuestManager.quests.get("Tutorial").complete)
    }

    @Test
    fun viewMapByAliasIgnoringResponseRequest() {
        val input = "commands && m"
        runBlocking { CommandParsers.parseCommand(GameState.player, input) }

        val expected = """
        An Open Field is a part of Kanbara Countryside. It is neighbored by:
        Name             Distance  Direction Path
        Apple Tree       100       N
        Barren Patch     100       S
        Training Circle  100       E
        Farmer's Hut     100       W
        Windmill         180       NE
        """.trimIndent().trim()

        assertEquals(expected, GameLogger.getMainHistory().getLastOutput().split("\n").joinToString("\n") { it.trim() }.trimIndent().trim())
    }

    @Test
    fun poisonSelf() {
        runBlocking {
            CommandParsers.parseCommand(GameState.player, "poison 1 for 5 on head of self")
            assertEquals(1, GameState.player.thing.soul.getNonSoundConditions().size)
            assertTrue(GameLogger.getMainHistory().contains("Poison decreases Your Health by 1 (9/10)."))

            CommandParsers.parseCommand(GameState.player, "wait 5")
            CommandParsers.parseCommand(GameState.player, "wait 1")
            assertEquals(0, GameState.player.thing.soul.getNonSoundConditions().size)
            assertEquals(5, GameState.player.thing.soul.getCurrent(HEALTH))
            assertTrue(GameLogger.getMainHistory().contains("Player is no longer Poisoned."))
        }
    }

    @Test
    fun feelTheRain() {
        runBlocking { CommandParsers.parseCommand(GameState.player, "rest 1 && debug weather gentle rain && rest 1") }
        assertEquals(1, GameState.player.thing.soul.getConditions().filter { it.name == "Rain Wet" }.size)
    }

    @Test
    fun lightTheWay() {
        runBlocking {
            CommandParsers.parseCommand(
                GameState.player,
                "w && s && mv to range && take tinder && rest 10 && n && rest 10 && e && n && w && take lantern && t mouth && hold lantern f && ls"
            )
            assertEquals("It's too dark to see anything.", GameLogger.getMainHistory().getLastOutput())
            CommandParsers.parseCommand(GameState.player, "use tinder on lantern && ls")
            assertEquals("It contains Wall Crack.", GameLogger.getMainHistory().getLastOutput())
        }
    }

    @Test
    fun craftDagger() {
        runBlocking {
            CommandParsers.parseCommand(
                GameState.player,
                "w && n && w && debug recipe && mv to chest && take all from chest && mv to forge && take tinder && debug stat smithing 2 && use tinder on forge && craft dagger"
            )

            val dagger = GameState.player.inventory.getItem("Bronze Dagger")
            assertNotNull(dagger)

            val tags = dagger.properties.tags
            assertTrue(tags.hasAll(listOf("Bronze", "Weapon")))
        }
    }

    @Test
    fun farmerTendsWheat() {
        runBlocking {
            GameState.putDebug(DebugType.CLARITY, true)
            CommandParsers.parseCommand(
                GameState.player,
                "rs 8 && w && rs 1 && e && rs 1 && rs 10 && mv wheat"
            )

            assertTrue(GameLogger.getMainHistory().contains("Farmer tends the Wheat Field."))
        }
    }

    @Test
    fun farmerSleeps() {
        runBlocking {
            GameState.putDebug(DebugType.CLARITY, true)
            CommandParsers.parseCommand(
                GameState.player,
                "rs 3 && w && s && rs 1 && rs 10 && mv bed"
            )

            assertTrue(GameLogger.getMainHistory().contains("Farmer rests for 10 hours."))
        }
    }
}
