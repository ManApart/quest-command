package crafting.material

import core.utility.Named
import magic.Element

//Density 0 is void, 100 is diamond
//Roughness 0 is perectly smooth, 100 is sandpaper
val DEFAULT_MATERIAL = Material("Void", 0, 0) { 0 }

data class Material(
    override val name: String,
    val density: Int,
    val roughness: Int,
    val getInteractionStrength: (Element) -> Int
) : Named