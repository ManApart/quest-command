package core.gameState

import core.utility.apply

class Tags(tags: List<String> = listOf()) {
    constructor(base: Tags, params: Map<String, String> = mapOf()) : this(base.tags.apply(params))

    private val tags = tags.toMutableList()

    override fun toString(): String {
        return tags.joinToString(", ")
    }

    fun has(tag: String): Boolean {
        return lowerCaseTags().contains(tag.toLowerCase())
    }

    fun add(tag: String) {
        if (!has(tag)) {
            tags.add(tag)
        }
    }

    fun remove(tag: String) {
        tags.remove(tag)
    }

    fun hasAll(other: Tags): Boolean {
        return other.lowerCaseTags().all { this.lowerCaseTags().contains(it) }
    }

    fun hasAny(other: Tags): Boolean {
        return other.lowerCaseTags().any { this.lowerCaseTags().contains(it) }
    }

    fun hasNone(other: Tags): Boolean {
        return !other.lowerCaseTags().any { this.lowerCaseTags().contains(it) }
    }

    fun matches(other: Tags): Boolean {
        return hasAll(other) && other.hasAll(this)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty()
    }

    fun getAll(): List<String> {
        return tags.toList()
    }

    private fun lowerCaseTags(): List<String> {
        return tags.asSequence().map { it.toLowerCase() }.toList()
    }
}