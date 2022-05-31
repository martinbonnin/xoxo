package xoxo

import org.junit.Test
import java.io.File

class SessionizeTest {
    @Test
    fun test() {
        File("test-fixtures/sessionize.xml").toXmlDocument()
            .root // <div class="sz-root">
            .walkElements()
            .filter {
                it.attributes.containsKey("data-sessionid")
            }
            .onEach {
                val room = it.walkElements().first { it.attributes["class"] == "sz-session__room" }.firstNonBlankTextContent()
                // "TimeWithDuration|en-US|2022-06-02T15:00:00.0000000Z|2022-06-02T16:30:00.0000000Z"
                val sztz = it.walkElements().mapNotNull { it.attributes["data-sztz"] }.first()
                val parts = sztz.split("|")
                val language = parts[1]
                val start = parts[2]
                val end = parts[3]
                val title = it.walkElements().first { it.attributes["class"] == "sz-session__title" }.firstNonBlankTextContent()
                    .replace("\n", "")
                    .replace(Regex("  *"), " ")

                println("$title - $language - $room")
                println("$start -> $end")
                it.walkElements().filter { it.attributes.containsKey("data-speakerid") }.map {
                    val name = it.firstNonBlankTextContent()
                    println("$name")
                }.toList()
            }.toList()
    }
}
