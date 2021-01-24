package resources.locationDescriptions

import core.GameState
import core.conditional.ConditionalString
import core.conditional.StringOption
import traveling.location.location.LocationDescriptionResource

class LocationDescriptions : LocationDescriptionResource {
    override val values: List<ConditionalString> = listOf(
            ConditionalString("Field", listOf(
                    StringOption("The red sun fills the waving grasses, making them glow. They sway like licks of flame.") {
                        GameState.timeManager.getHour() in 20..30 || GameState.timeManager.getHour() in 70..80
                    },
                    StringOption("You feel the waist high grasses brush against you. You can hear their rustle expand into the distance.") {
                        GameState.timeManager.isNight()
                    },
                    StringOption("The waist high grasses stretch into the distance. They don't obscure your view, but as they drift in the wind they give you the sensation of floating on a calm sea.") { true },
            ))
    )
}