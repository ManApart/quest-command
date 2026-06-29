import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = true
    classDiscriminator = "type"
}


fun readConfig(): Config {
    val configFile = File("./tool-config.json")
    return if (configFile.exists()) {
        jsonMapper.decodeFromString(configFile.readText())
    } else {
        configFile.writeText(jsonMapper.encodeToString(Config()))
        throw IllegalStateException("Please fill out the config file!")
    }
}

@Serializable
data class Config(
    val dryRun: Boolean = true,
    val fancyReplace: FancyReplaceConfig = FancyReplaceConfig(),
)


@Serializable
data class FancyReplaceConfig(
    val functionName: String = "",
    val keywords: List<String> = emptyList(),
)
