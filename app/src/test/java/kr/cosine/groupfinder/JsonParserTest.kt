package kr.cosine.groupfinder

import com.google.gson.JsonParser
import org.junit.Test
import java.io.FileReader

class JsonParserTest {

    @Test
    fun json_parser_test() {
        val fileReader = FileReader("C:\\Users\\rhdwl\\Downloads\\Champion.json")
        val baseJsonObject = JsonParser.parseReader(fileReader).asJsonObject
        val dataJsonObject = baseJsonObject.getAsJsonObject("data")
        println("size: ${dataJsonObject.keySet().size}")
        dataJsonObject.keySet().forEach { championEnglishName ->
            val championJsonObject = dataJsonObject.getAsJsonObject(championEnglishName)
            val championId = championJsonObject.get("key").asString
            val championKoreanName = championJsonObject.get("name").asString
            println("\"$championId\" to Pair(\"$championEnglishName\", \"$championKoreanName\")")
        }
    }
}