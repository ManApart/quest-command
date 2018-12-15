package core.gameState

import core.utility.replaceParams

class Tags(tags: List<String> = listOf()) {
    private val tags = tags.asSequence().map { it.toLowerCase() }.toMutableList()

    fun has(tag: String): Boolean {
        return tags.contains(tag.toLowerCase())
    }

    fun add(tag: String) {
        tags.add(tag.toLowerCase())
    }

    fun remove(tag: String) {
        tags.remove(tag.toLowerCase())
    }

    fun hasAll(other: Tags): Boolean {
        return hasAll(other.tags)
    }

    fun hasAll(tags: List<String>): Boolean {
        tags.forEach {
            if (!this.tags.contains(it)) {
                return false
            }
        }
        return true
    }

    fun hasNone(tags: List<String>): Boolean {
        tags.forEach {
            if (this.tags.contains(it)) {
                return false
            }
        }
        return true
    }

    fun matches(other: Tags): Boolean {
        tags.forEach {
            if (!other.has(it)) {
                return false
            }
        }
        other.tags.forEach {
            if (!has(it)) {
                return false
            }
        }
        return true
    }

    fun inherit(parent: Tags) {
        parent.tags.forEach {
            if (!tags.contains(it)) {
                tags.add(it)
            }
        }
    }

    fun applyParams(params: Map<String, String>): Tags {
        val newTags = tags.map { replaceParams(it, params) }
        return Tags(newTags)
    }
}