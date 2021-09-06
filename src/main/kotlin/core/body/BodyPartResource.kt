package core.body

import traveling.location.location.LocationRecipeBuilder

interface BodyPartResource {
    val values: List<LocationRecipeBuilder>
}