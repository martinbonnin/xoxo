package xoxo

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class XmlNodeTest {

    @Test
    fun `walk nodes`() {
        @Language("XML")
        val xml = """<root><a>1</a><b><b1>2′</b1><b2>2″</b2><b3>2‴</b3></b><c>3</c></root>""".trimIndent()
        val nodes = xml.toXmlDocument().root.walk().toList()
        with(nodes.iterator()) {
            assertEquals("root", (next() as XmlElement).name)
            assertEquals("a", (next() as XmlElement).name)
            assertEquals("1", (next() as XmlText).content)
            assertEquals("b", (next() as XmlElement).name)
            assertEquals("b1", (next() as XmlElement).name)
            assertEquals("2′", (next() as XmlText).content)
            assertEquals("b2", (next() as XmlElement).name)
            assertEquals("2″", (next() as XmlText).content)
            assertEquals("b3", (next() as XmlElement).name)
            assertEquals("2‴", (next() as XmlText).content)
            assertEquals("c", (next() as XmlElement).name)
            assertEquals("3", (next() as XmlText).content)
            assertFalse(hasNext())
        }
    }

}