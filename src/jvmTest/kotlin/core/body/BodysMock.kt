package core.body

class BodysMock(override val values: List<BodyBuilder> = listOf()) : BodysCollection {
    companion object {
        fun fromPart(vararg parts: String): BodysMock {
            return BodysMock(listOf(BodyBuilder("body").apply{
                parts(parts.toList())
            }))
        }
    }
}
