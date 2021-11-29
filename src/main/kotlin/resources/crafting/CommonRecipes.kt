package resources.crafting

import crafting.RecipeResource
import crafting.recipes

class CommonRecipes : RecipeResource {
    override val values = recipes {
        recipe("Sliced Food") {
            verb("slice")
            skill("Cooking", 1)
            ingredient("Food", "Slicable")
            toolProps {
                tag("Sharp")
            }
            result {
                id(0)
                addTag("Sliced")
                removeTag("Slicable")
            }
        }

        recipe("Roasted Food") {
            verb("roast")
            skill("Cooking", 1)
            ingredient("Raw")
            toolProps {
                tag("Burning")
            }
            result {
                id(0)
                addTag("Roasted")
                removeTag("Raw")
            }
        }

        recipe("Cooked Food") {
            verb("cook")
            skill("Cooking", 2)
            ingredient("Raw")
            toolProps {
                tag("Range")
            }
            result {
                id(0)
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
            result {
                name("Bucket of Water")
            }
        }

        recipe("Dough") {
            verb("mix")
            ingredientNamed("Wheat Flour")
            ingredientNamed("Bucket of Water")
            result {
                name("Bucket")
                name("Dough")
            }
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
            result {
                name("Apple Pie")
            }
        }
    }
}