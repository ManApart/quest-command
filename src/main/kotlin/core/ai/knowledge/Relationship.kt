package core.ai.knowledge

data class Relationship(val source: String, val relatesTo: String, val kind: String, val confidence: Int, val amount: Int = 0) {
}