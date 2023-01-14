package magic

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.GameState.player
import core.events.EventManager
import core.thing.Thing
import createMockedGame
import kotlinx.coroutines.runBlocking
import magic.castSpell.CastCommand
import magic.castSpell.getThingedPartsOrAll
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import kotlin.test.Test


import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.position.ThingAim
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CastCommandCastTest {

    companion object {
        init {
            createMockedGame()
        }

        private val thingA = Thing("thingA")
        private val thingB = Thing("thingB")
        private val player = GameManager.newPlayer()
        private val scope = GameState.player.thing.currentLocation()

        init {
            scope.addThing(thingA)
            scope.addThing(thingB)
        }

    }

    @BeforeTest
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun castWord() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        runBlocking { CastCommand().execute(player, "cast", "testspellA".split(" ")) }

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.things.isEmpty())
    }

    @Test
    fun castWordWithParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        runBlocking { CastCommand().execute(player, "cast", "testspellA 1 2".split(" ")) }

        assertEquals("1 2", spellCommand.args.fullString)
        assertTrue(spellCommand.things.isEmpty())
    }

    @Test
    fun castWordWithThing() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        runBlocking { CastCommand().execute(player, "cast", "testspellA on thingA".split(" ")) }

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(thingsContainByName(spellCommand.things, thingA))
    }

    @Test
    fun castWordWithThingsAndParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        runBlocking { CastCommand().execute(player, "cast", "testspellA 1 2 on thingA and thingB".split(" ")) }

        assertEquals("1 2", spellCommand.args.fullString)
        assertTrue(thingsContainByName(spellCommand.things, thingA))
        assertTrue(thingsContainByName(spellCommand.things, thingB))
    }

    private fun thingsContainByName(thingAims: List<ThingAim>, thing: Thing): Boolean {
        return thingAims.any { it.thing.name == thing.name }
    }

    @Test
    fun limitParts() {
        val part = Location(LocationNode("leg"))
        val thing = ThingAim(Thing("Bob"), listOf(part))

        val results = getThingedPartsOrAll(thing, 3)

        assertEquals(1, results.size)
        assertEquals(part, results.first())
    }


}
