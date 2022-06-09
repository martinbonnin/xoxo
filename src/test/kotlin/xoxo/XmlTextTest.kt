package xoxo

import org.intellij.lang.annotations.Language
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.text.Typography.ellipsis

class XmlTextTest {

    @Test
    fun `toString does ellipsize if too long`() {
        @Language("XML")
        val xml = """
            <root>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.</root>
        """.trimIndent()
        val text = xml.toXmlDocument().root.walk().filterIsInstance<XmlText>().single()
        assertEquals(
            "Lorem ipsum dolor sit amet, consectetur adipiscing$ellipsis",
            text.toString()
        )
    }

    @Test
    fun `toString does not ellipsize if not too long`() {
        @Language("XML")
        val xml = """
            <root>Lorem ipsum dolor sit amet.</root>
        """.trimIndent()
        val text = xml.toXmlDocument().root.walk().filterIsInstance<XmlText>().single()
        assertEquals(
            "Lorem ipsum dolor sit amet.",
            text.toString()
        )
    }

}