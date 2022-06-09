package xoxo

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test

class XmlElementTest {

    @Test
    fun `toString with attributes`() {
        @Language("XML")
        val xml = """<root key1="value1" key2="value2"/>"""
        val text = xml.toXmlDocument().root.walkElements().single()
        assertEquals(
            "<root {key1=value1, key2=value2}>",
            text.toString()
        )
    }

    @Test
    fun `toString without attributes`() {
        @Language("XML")
        val xml = """<root/>"""
        val text = xml.toXmlDocument().root.walkElements().single()
        assertEquals(
            "<root>",
            text.toString()
        )
    }

}