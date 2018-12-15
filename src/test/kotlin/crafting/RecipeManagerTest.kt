package crafting

import core.gameState.*
import org.junit.Assert
import org.junit.Test
import system.DependencyInjector

class RecipeManagerTest {

    @Test
    fun findRecipe() {
        val recipe = Recipe("Baked Apple", listOf("Apple"), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), "Baked Apple")
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range", properties =  Properties(tags = Tags(listOf("Range"))))
        val baker = Player()


        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result =  RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertEquals(recipe, result)
    }

    @Test
    fun recipeWithoutIngredientsNotFound() {
        val recipe = Recipe("Baked Apple", listOf("Apple"), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), "Baked Apple")
        val ingredients = listOf<Item>()
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result =  RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutCorrectIngredientsNotFound() {
        val recipe = Recipe("Poor Quality Cooked Meat", listOf("Raw Poor Quality Meat"), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), "Poor Quality Cooked Meat")
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result =  RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutToolNotFound() {
        val recipe = Recipe("Baked Apple", listOf("Apple"), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), "Baked Apple")
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("NONE")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result =  RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutSkillNotFound() {
        val recipe = Recipe("Baked Apple", listOf("Apple"), mapOf(Pair("Cooking", 5)), Properties(tags = Tags(listOf("Range"))), "Baked Apple")
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result =  RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }


}