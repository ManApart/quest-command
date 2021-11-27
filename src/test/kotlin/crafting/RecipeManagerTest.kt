package crafting

import core.DependencyInjector
import core.GameManager
import core.properties.Properties
import core.properties.Tags
import core.thing.Thing
import createMockedGame
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RecipeManagerTest {

    @Before
    fun setup() {
        createMockedGame()
    }

    @Test
    fun findRecipe() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredientNamed("Apple")
            skill("Cooking", 1)
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
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByTags() {
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
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByItemWithTags() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredient("Apple", listOf("Raw", "fruit"))
        }
        val recipe = recipeBuilder.build()
        val ingredients = listOf(Thing("Apple", properties = Properties(tags = Tags("Raw", "Fruit"))))
        val tool = Thing("Range", properties = Properties(tags = Tags("Range")))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(baker.thing, ingredients, tool)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun recipeWithoutIngredientsNotFound() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredientNamed("Apple")
            skill("Cooking", 1)
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
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutCorrectIngredientsNotFound() {
        val recipeBuilder = recipe("Poor Quality Cooked Meat") {
            ingredientNamed("Raw Poor Quality Meat")
            skill("Cooking", 1)
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
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutToolNotFound() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredientNamed("Apple")
            skill("Cooking", 1)
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
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutSkillNotFound() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredientNamed("Apple")
            skill("Cooking", 5)
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
        Assert.assertTrue(results.isEmpty())
    }


}