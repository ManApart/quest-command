package crafting

import core.DependencyInjector
import core.GameManager
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import createMockedGame
import kotlinx.coroutines.runBlocking


import status.stat.COOKING
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecipeManagerTest {

    @BeforeTest
    fun setup() {
        createMockedGame()
    }

    @Test
    fun findRecipe() {
        runBlocking {
            val recipeBuilder = recipe("Baked Apple") {
                ingredientNamed("Apple")
                skill(COOKING, 1)
                toolProps { tag("Range") }
                result("Baked Apple")
            }
            val recipe = recipeBuilder.build()
            val ingredients = listOf(Thing("Apple"))
            val tool = Thing("Range", properties = Properties(tags = Tags("Range")))
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertEquals(recipe, results.first())
        }
    }

    @Test
    fun findRecipeByTags() {
        runBlocking {
            val recipeBuilder = recipe("Baked Pear") {
                ingredient("Raw", "fruit")
            }
            val recipe = recipeBuilder.build()

            val ingredients = listOf(Thing("Pear", properties = Properties(tags = Tags("Raw", "Fruit"))))
            val tool = Thing("Range", properties = Properties(tags = Tags("Range")))
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertEquals(recipe, results.first())
        }
    }

    @Test
    fun findRecipeByItemWithTags() {
        runBlocking {
            val recipeBuilder = recipe("Baked Apple") {
                ingredient {
                    name("Apple")
                    tag("Raw", "Fruit")
                }
            }
            val recipe = recipeBuilder.build()
            val ingredients = listOf(Thing("Apple", properties = Properties(tags = Tags("Raw", "Fruit"))))
            val tool = Thing("Range", properties = Properties(tags = Tags("Range")))
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertEquals(recipe, results.first())
        }
    }

    @Test
    fun recipeWithoutIngredientsNotFound() {
        runBlocking {
            val recipeBuilder = recipe("Baked Apple") {
                ingredientNamed("Apple")
                skill(COOKING, 1)
                toolProps { tag("Range") }
                result("Baked Apple")
            }
            val ingredients = listOf<Thing>()
            val tool = Thing("Range")
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertTrue(results.isEmpty())
        }
    }

    @Test
    fun recipeWithoutCorrectIngredientsNotFound() {
        runBlocking {
            val recipeBuilder = recipe("Poor Quality Cooked Meat") {
                ingredientNamed("Raw Poor Quality Meat")
                skill(COOKING, 1)
                toolProps { tag("Range") }
                result("Poor Quality Cooked Meat")
            }
            val ingredients = listOf(Thing("Apple"))
            val tool = Thing("Range")
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertTrue(results.isEmpty())
        }
    }

    @Test
    fun recipeWithoutToolNotFound() {
        runBlocking {
            val recipeBuilder = recipe("Baked Apple") {
                ingredientNamed("Apple")
                skill(COOKING, 1)
                toolProps { tag("Range") }
                result("Baked Apple")
            }
            val ingredients = listOf(Thing("Apple"))
            val tool = Thing("NONE")
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertTrue(results.isEmpty())
        }
    }

    @Test
    fun recipeWithoutSkillNotFound() {
        runBlocking {
            val recipeBuilder = recipe("Baked Apple") {
                ingredientNamed("Apple")
                skill(COOKING, 5)
                toolProps { tag("Range") }
                result("Baked Apple")
            }
            val ingredients = listOf(Thing("Apple"))
            val tool = Thing("Range", properties = Properties(tags = Tags("Range")))
            val baker = GameManager.newPlayer()

            val fakeParser = RecipesMock(listOf(recipeBuilder))
            DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
            RecipeManager.reset()

            val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
            assertTrue(results.isEmpty())
        }
    }


}