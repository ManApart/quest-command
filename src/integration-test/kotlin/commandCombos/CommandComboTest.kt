package commandCombos

import core.commands.CommandParser
import core.gameState.GameState
import core.history.ChatHistory
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.EventManager
import system.GameManager
import kotlin.test.assertEquals

class CommandComboTest {

    companion object {
        @BeforeClass @JvmStatic
        fun setup() {
            EventManager.registerListeners()
        }
    }

    @Before
    fun reset() {
        GameManager.newGame()
        EventManager.executeEvents()
    }

    @Test
    fun sliceApple() {
        val input = "use dagger on apple"
        CommandParser.parseCommand(input)
        assertEquals("You slice Apple and get Sliced Apple.", ChatHistory.getLastOutput())
    }

    @Test
    fun roastApple() {
        val input = "w && s && pickup tinder box && n && e && n && use tinder on tree && pickup apple && use apple on tree"
        CommandParser.parseCommand(input)
        assertEquals("You roast Apple and get Roasted Apple.", ChatHistory.getLastOutputs()[ChatHistory.getLastOutputs().size-3])
    }

    @Test
    fun cookApple() {
        val stat = GameState.player.creature.soul.getStats().first { it.name.toLowerCase() == "cooking" }
        stat.setLevel(2)

        val input = "w && s && cook apple on range"
        CommandParser.parseCommand(input)
        assertEquals("You cook Apple and get Cooked Apple.", ChatHistory.getLastOutput())
    }

    @Test
    fun chopTree() {
        val input = "n && pickup hatchet && equip hatchet && chop tree"
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
        val input = "s && slash rat && slash rat && slash rat"
        CommandParser.parseCommand(input)
        assertEquals("The battle ends.", ChatHistory.getLastOutput())
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

//    @Test
//    fun makePie() {
//        val input = "slash wheat && pickup wheat && t hut && take bucket && use bucket on well && t windmill && t" +
//                "&& a && a && place wheat in chute && d && d && take wheat from bin && use flour on bucket" +
//                "&& t tree && t && take apple and use dagger on apple" +
//                "&& t interior && t && t && take pie tin" +
//                "&& craft apple pie"
//        CommandParser.parseCommand(input)
//        assertEquals("Player picked up Wheat Flour.", ChatHistory.getLastOutput())
//    }

}