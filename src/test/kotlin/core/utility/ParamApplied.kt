package core.utility

data class ParamApplied(val topLevelString: String = "",
                        val number: Int = 1,
                        val topLevelList: List<String> = listOf(),
                        val topLevelMap: Map<String, String> = mapOf()

)