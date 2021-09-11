package crafting

import core.DependencyInjector
import core.GameManager
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import createMockedGame
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByTags() {
        val recipeBuilder = recipe("Baked Pear") {
            ingredient("Raw", "fruit")
        }
        val recipe = recipeBuilder.build()

        val ingredients = listOf(Target("Pear", properties = Properties(tags = Tags(listOf("Raw", "Fruit")))))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByItemWithTags() {
        val recipeBuilder = recipe("Baked Apple") {
            ingredient("Apple", listOf("Raw", "fruit"))
        }
        val recipe = recipeBuilder.build()
        val ingredients = listOf(Target("Apple", properties = Properties(tags = Tags(listOf("Raw", "Fruit")))))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
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
        val ingredients = listOf<Target>()
        val tool = Target("Range")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
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
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
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
        val ingredients = listOf(Target("Apple"))
        val tool = Target("NONE")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
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
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipesMock(listOf(recipeBuilder))
        DependencyInjector.setImplementation(RecipesCollection::class, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertTrue(results.isEmpty())
    }


}