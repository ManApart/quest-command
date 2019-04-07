package combat.battle.position

class TargetPosition(private val horizontal: Horizontal = Horizontal.CENTER, private val vertical: Vertical = Vertical.CENTER) {

    override fun toString(): String {
        return if (vertical == Vertical.CENTER && horizontal == Horizontal.CENTER){
            "center"
        } else {
            vertical.toString() + " " + horizontal.toString()
        }
    }

    fun equals(other: TargetPosition): Boolean {
        return  horizontal == other.horizontal && vertical == other.vertical
    }

    fun shift(other: TargetPosition) : TargetPosition {
        return TargetPosition(this.horizontal.shift(other.horizontal), this.vertical.shift(other.vertical))
    }

    fun invert() : TargetPosition {
        return TargetPosition(this.horizontal.invert(), this.vertical.invert())
    }

    fun getHitLevel(other: TargetPosition) : HitLevel {
        return when {
            this.vertical == other.vertical && this.horizontal == other.horizontal -> HitLevel.DIRECT
            this.vertical == other.vertical || this.horizontal == other.horizontal -> HitLevel.GRAZING
            else -> HitLevel.MISS
        }
    }

}