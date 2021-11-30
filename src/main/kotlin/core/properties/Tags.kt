package core.properties

import core.utility.apply

data class Tags(private val tags: MutableList<String> = mutableListOf()) {
    constructor(vararg tags: String) : this(tags.toMutableList())
    constructor(base: Tags, params: Map<String, String> = mapOf()) : this(base.tags.apply(params).toMutableList())

    override fun toString(): String {
        return tags.joinToString(", ")
    }

    override fun equals(other: Any?): Boolean {
        return other is Tags && tags.sorted() == other.tags.sorted()
    }

    override fun hashCode(): Int {
        return tags.hashCode()
    }

    fun has(tag: String): Boolean {
        return lowerCaseTags().contains(tag.lowercase())
    }

    fun add(vararg tags: String) {
        tags.forEach { add(it) }
    }

    fun add(tag: String) {
        if (!has(tag)) {
            tags.add(tag)
        }
    }

    fun addAll(other: Tags) {
        other.tags.forEach { add(it) }
    }

    fun addAll(other: List<String>) {
        other.forEach { add(it) }
    }

    fun remove(tag: String) {
        tags.remove(tag)
    }

    fun removeAll(other: Tags) {
        other.tags.forEach { remove(it) }
    }

    fun removeAll(other: List<String>) {
        other.forEach { remove(it) }
    }

    fun hasAll(other: Tags): Boolean {
        return other.lowerCaseTags().all { this.lowerCaseTags().contains(it) }
    }

    fun hasAll(other: List<String>): Boolean {
        return other.map { it.lowercase() }.all { this.lowerCaseTags().contains(it) }
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

    fun clear() {
        tags.clear()
    }

    private fun lowerCaseTags(): List<String> {
        return tags.asSequence().map { it.lowercase() }.toList()
    }

}