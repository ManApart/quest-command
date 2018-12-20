package crafting

import core.gameState.*
import org.junit.Assert
import org.junit.Test
import system.DependencyInjector
import kotlin.test.fail

class RecipeManagerTest {

    @Test
    fun findRecipe() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = Player()


        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertEquals(recipe, result)
    }

    @Test
    fun findRecipeByTags() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient(tags = Tags(listOf("Raw", "Fruit")))))
        val ingredients = listOf(Item("Apple", properties = Properties(Tags(listOf("Raw", "Fruit")))))
        val tool = Activator("Range", properties = Properties(tags = Tags(listOf("Range"))))
        val baker = Player()


        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertEquals(recipe, result)
    }

    @Test
    fun findRecipeByItemWithTags() {
        fail()
    }

    //TODO - Move recipe returns to cook listener
    @Test
    fun recipeReturnsNewItem() {
        fail()
    }

    @Test
    fun recipeReturnsNewItems() {
        fail()
    }

    @Test
    fun recipeReturnsFirstItemWithDifferentTags() {
        fail()
    }

    @Test
    fun recipeWithoutIngredientsNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf<Item>()
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutCorrectIngredientsNotFound() {
        val recipe = Recipe("Poor Quality Cooked Meat", listOf(RecipeIngredient("Raw Poor Quality Meat")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Poor Quality Cooked Meat")))
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutToolNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 1)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("NONE")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }

    @Test
    fun recipeWithoutSkillNotFound() {
        val recipe = Recipe("Baked Apple", listOf(RecipeIngredient("Apple")), mapOf(Pair("Cooking", 5)), Properties(tags = Tags(listOf("Range"))), listOf(RecipeResult("Baked Apple")))
        val ingredients = listOf(Item("Apple"))
        val tool = Activator("Range")
        val baker = Player()

        val fakeParser = RecipeFakeParser(listOf(recipe))
        DependencyInjector.setImplementation(RecipeParser::class.java, fakeParser)
        RecipeManager.reset()

        val result = RecipeManager.findRecipe(ingredients, tool, baker.creature.soul)
        Assert.assertNull(result)
    }


}