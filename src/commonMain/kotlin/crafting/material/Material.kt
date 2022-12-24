package crafting.material

import core.ai.behavior.Behavior
import core.properties.Properties
import core.utility.Named
import magic.Element

val DEFAULT_MATERIAL = Material("Void", 0, 0, Properties()) { 0 }

//Density 0 is void, 100 is diamond
//Roughness 0 is perectly smooth, 100 is sandpaper
//Interaction strength 0 is unaffected and 100 is radically affected

data class Material(
    override val name: String,
    val density: Int,
    val roughness: Int,
    val properties: Properties,
    val behaviors: List<Behavior<*>> = listOf(),
    val getInteractionStrength: (Element) -> Int //Do we need this alongside behaviors?
) : Named