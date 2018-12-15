package building.json

class JsonConverter(val inputPath: String, val outputPath: String) {

    fun convert(){
        println("Convert '$inputPath' to '$outputPath'")
    }
}