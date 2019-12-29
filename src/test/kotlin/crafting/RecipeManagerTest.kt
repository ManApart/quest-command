package crafting

import core.DependencyInjector
import core.GameManager
import core.properties.Properties
import core.properties.Tags
import core.target.Target
import org.junit.Assert
import org.junit.Test

class RecipeManagerTest {

    @Test
    fun findRecipe() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByTags() {
        val recipe = Recipe("Baked Pear", listOf(RecipeIngredient(tags = Tags(listOf("Raw", "Fruit")))))
        val ingredients = listOf(Target("Pear", properties = Properties(tags = Tags(listOf("Raw", "Fruit")))))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun findRecipeByItemWithTags() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple", tags = Tags(listOf("Raw", "Fruit")))))
        val ingredients = listOf(Target("Apple", properties = Properties(tags = Tags(listOf("Raw", "Fruit")))))
        val tool = Target("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertEquals(recipe, results.first())
    }

    @Test
    fun recipeWithoutIngredientsNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf<Target>()
        val tool = Target("Range")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutCorrectIngredientsNotFound() {
        val recipe = Recipe("Poor Quality Cooked Meat", listOf(RecipeIngredient("Raw Poor Quality Meat")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Poor Quality Cooked Meat")))
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutToolNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Target("Apple"))
        val tool = Target("NONE")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun recipeWithoutSkillNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 5)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Target("Apple"))
        val tool = Target("Range")
        val baker = GameManager.newPlayer()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val results = RecipeManager.findCraftableRecipes(ingredients, tool, baker.soul)
        Assert.assertTrue(results.isEmpty())
    }


}