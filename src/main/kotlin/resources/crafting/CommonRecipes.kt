package resources.crafting

import crafting.RecipeResource
import crafting.recipes

class CommonRecipes : RecipeResource {
    override val values = recipes {
        recipe("Sliced Food") {
            verb("slice")
            skill("Cooking", 1)
            ingredient("Base"){
                tag("Food", "Slicable")
            }
            toolProps {
                tag("Sharp")
            }
            result {
                reference("Base")
                addTag("Sliced")
                removeTag("Slicable")
            }
        }

        recipe("Roasted Food") {
            verb("roast")
            skill("Cooking", 1)
            ingredient("Base"){
                tag("Raw")
            }
            toolProps {
                tag("Burning")
            }
            result {
                reference("Base")
                addTag("Roasted")
                removeTag("Raw")
            }
        }

        recipe("Cooked Food") {
            verb("cook")
            skill("Cooking", 2)
            ingredient("Base"){
                tag("Raw")
            }
            toolProps {
                tag("Range")
            }
            result {
                reference("Base")
                addTag("Cooked")
                removeTag("Raw")
            }
        }

        recipe("Bucket of Water") {
            verb("fill")
            ingredientNamed("Bucket")
            toolProps {
                tag("Water Source")
            }
            result("Bucket of Water")
        }

        recipe("Dough") {
            verb("mix")
            ingredientNamed("Wheat Flour")
            ingredientNamed("Bucket of Water")
            result("Bucket")
            result("Dough")
        }

        recipe("Apple Pie") {
            verb("bake")
            ingredient {
                name("Apple")
                tag("Sliced")
            }
            ingredientNamed("Pie Tin")
            ingredientNamed("Dough")
            skill("Cooking" to 2)
            toolProps {
                tag("Range", "Burning")
            }
            result("Apple Pie")
        }
    }
}