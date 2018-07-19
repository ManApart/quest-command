package core.utility

object RandomManager {

    fun isSuccess(chance: Double) : Boolean {
        if (chance <= 0){
            return false
        } else if (chance >= 100){
            return true
        }

        val rand = Math.random()
        return chance >= rand
    }
}