package xoxo

import okio.Buffer
import okio.BufferedSource
import okio.buffer
import okio.source
import org.w3c.dom.*
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

sealed interface XmlNode {
    val asElement: XmlElement
        get() = this as XmlElement

    val asText: XmlText
        get() = this as XmlText
}

private fun Node.toXmlNode(): XmlNode? {
    return when (this) {
        is Element -> XmlElement(this)
        is Text -> XmlText(this)
        else -> null
    }
}

class XmlElement internal constructor(private val element: Element) : XmlNode {
    val name: String
        get() = element.tagName
    val children: List<XmlNode>
        get() = element.childNodes.toList().mapNotNull { it.toXmlNode() }
    val childElements: List<XmlElement>
        get() = children.filterIsInstance<XmlElement>()
    val attributes: Map<String, String>
        get() = element.attributes.toList().filterIsInstance<Attr>().associate {
            it.name to it.value
        }
    val textContent: String
        get() = walk().filterIsInstance<XmlText>().joinToString("") { it.content }

    override fun toString(): String {
        return "<$name $attributes>"
    }
}

private fun NamedNodeMap.toList(): List<Node> {
    return 0.until(this.length).map {
        this.item(it)
    }
}


class XmlText internal constructor(private val text: Text) : XmlNode {
    val content: String
        get() = text.textContent

    override fun toString(): String {
        return content.substring(0, 50) + "..."
    }
}

private fun NodeList.toList(): List<Node> {
    return 0.until(this.length).map {
        this.item(it)
    }
}

class XmlDocument internal constructor(private val document: Document) {
    val root: XmlElement
        get() {
            return XmlElement(document.documentElement)
        }
}

fun BufferedSource.toXmlDocument(): XmlDocument {
    return XmlDocument(
        inputStream().use {
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it)
        }
    )
}

fun File.toXmlDocument(): XmlDocument {
    return source().buffer().toXmlDocument()
}

fun String.toXmlDocument(): XmlDocument {
    return Buffer().writeUtf8(this).toXmlDocument()
}

fun XmlNode.walk(): Sequence<XmlNode> {
    val stack = mutableListOf<XmlNode>()
    stack.add(this)
    return generateSequence {
        if (stack.isEmpty()) {
            return@generateSequence null
        }

        val element = stack.removeFirst()
        if (element is XmlElement) {
            stack.addAll(element.children)
        }
        element
    }
}

fun XmlElement.walkElements(): Sequence<XmlElement> {
    return walk().filterIsInstance<XmlElement>()
}

fun XmlElement.firstNonBlankTextContent(): String {
    return walk().filterIsInstance<XmlText>().map { it.content }.first { it.isNotBlank() }.trim()
}
