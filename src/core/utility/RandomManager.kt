package core.utility

object RandomManager {

    fun isSuccess(chance: Double) : Boolean {
        if (chance <= 0){
            return false
        }

        val rand = Math.random()
//        println("$chance, $rand")
        return chance >= rand
    }
}