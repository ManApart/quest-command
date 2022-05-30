package resources.crafting

import core.thing.thing
import crafting.RecipeResource
import crafting.recipes
import status.stat.*

class CommonRecipes : RecipeResource {
    override val values = recipes {
        recipe("Sliced Food") {
            verb("slice")
            skill(COOKING, 1)
            ingredient("Base") {
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
            skill(COOKING, 1)
            ingredient("Base") {
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
            skill(COOKING, 2)
            ingredient("Base") {
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
            skill(COOKING to 2)
            toolProps {
                tag("Range", "Burning")
            }
            result("Apple Pie")
        }

        recipe("Dagger") {
            ingredient("Ingot") {
                tag("Metal", "Ingot")
                matches { crafter, ingredient, _ ->
                    canSmith(crafter, ingredient, 2f)
                }
            }
            ingredient("Inlay") {
                optional()
                tag("Gem")
                skill(SMITHING to 10)
            }
            ingredientNamed("Leather") {
                skill(CRAFTSMANSHIP to 1)
            }
            skill(SMITHING to 1)
            toolProps {
                tag("Forge", "Burning")
            }
            result {
                description("A dagger of the type of metal given, optionally with its handle inlaid with a gem.")
                produces { _, _, usedIngredients ->
                    val ingot = usedIngredients["Ingot"]!!.second
                    val metalUsed = ingot.getMetal()!!
                    val metalQuality = ingot.getMetalQuality()
                    val inlay = usedIngredients["Inlay"]?.second
                    val inlayString = if (inlay != null) " inlaid with $inlay" else ""
                    thing("$metalUsed Dagger") {
                        description("A $metalUsed Dagger$inlayString.")
                        equipSlotOptions("Right Hand Grip")
                        equipSlotOptions("Left Hand Grip")
                        props {
                            value("weight", 1)
                            value("slashDamage", metalQuality)
                            value("stabDamage", (metalQuality * 1.5).toInt())
                            value("range", 2)
                            tag("Weapon", "Sharp", "Small", metalUsed)
                        }
                    }.build()
                }
            }
        }
    }
}