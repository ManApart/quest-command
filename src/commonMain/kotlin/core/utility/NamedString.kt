package core.utility

class NamedString(override val name: String) : Named {
    override fun toString(): String {
        return name
    }
}