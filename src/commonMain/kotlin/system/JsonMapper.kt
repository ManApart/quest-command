package system

import kotlinx.serialization.json.Json

val mapper = Json {
    encodeDefaults = false
}
