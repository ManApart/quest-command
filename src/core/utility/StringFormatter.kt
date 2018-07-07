package core.utility

object StringFormatter {

    fun format(value: Boolean, trueChoice: String, falseChoice: String) : String{
        return if(value) trueChoice else falseChoice
    }
}