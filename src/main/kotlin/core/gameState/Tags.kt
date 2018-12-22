package core.gameState

import core.utility.replaceParams

class Tags(tags: List<String> = listOf()) {
    private val tags = tags.toMutableList()

    override fun toString(): String {
        return if (tags.isEmpty()) {
            "No Tags"
        }else {
            "Tags: ${tags.joinToString(", ")}"
        }
    }

    fun has(tag: String): Boolean {
        return lowerCaseTags().contains(tag.toLowerCase())
    }

    fun add(tag: String) {
        tags.add(tag)
    }

    fun remove(tag: String) {
        tags.remove(tag)
    }

    fun hasAll(other: Tags): Boolean {
        return other.lowerCaseTags().all { this.lowerCaseTags().contains(it) }
    }

    fun hasNone(other: Tags): Boolean {
        return !other.lowerCaseTags().any { this.lowerCaseTags().contains(it) }
    }

    fun matches(other: Tags): Boolean {
        return hasAll(other) && other.hasAll(this)
    }

    fun inherit(parent: Tags) {
        parent.lowerCaseTags().forEach {
            if (!lowerCaseTags().contains(it)) {
                tags.add(it)
            }
        }
    }

    fun applyParams(params: Map<String, String>): Tags {
        val newTags = tags.map { replaceParams(it, params) }
        return Tags(newTags)
    }

    fun isEmpty() : Boolean{
        return tags.isEmpty()
    }

    fun getAll() : List<String> {
        return tags.toList()
    }

    private fun lowerCaseTags() : List<String> {
        return tags.asSequence().map { it.toLowerCase() }.toList()
    }
}