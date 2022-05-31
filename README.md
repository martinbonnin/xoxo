# Xoxo

Xoxo is a simple wrapper around [org.w3c.dom](https://docs.oracle.com/javase/8/docs/api/index.html?org/w3c/dom/package-summary.html) to parse XML using nice Kotlin APIs. No more `NodeList`, `.item()`, etc... just use `.children`, `.filterIsInstance<>()` and all the [Okio](https://github.com/square/okio) and Kotlin APIs we all love 💙.

Xoxo is designed for dynamic parsing in small JVM-only projects like [Kotlin scripts](https://kotlinlang.org/docs/custom-script-deps-tutorial.html) and does not pretend to cover the full XML specification nor XML editing.

## Installation

```kotlin
repositories {
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}
implementation("net.mbonnin.xoxo:xoxo:0.1-SNAPSHOT")
```

## Read the text content of the first <div> tag in a HTML file

```kotlin
File("index.html").toXmlDocument()
    .root
    .childElements
    .first { it.name == "body" }
    .childElements
    .first { it.name == "div" }
    .textContent
```

## Find all elements with a "videoId" attribute and display their duration

```kotlin
        """
            <root>
                <items>
                    <item videoId="1" title="title1">
                        <duration>30:30</duration>
                    </item>
                    <item videoId="2" title="title2">
                        <duration>31:00</duration>
                    </item>
                </items>
            </root>
        """.trimIndent()
    .toXmlDocument()
    .root
    .walkElements()
    .filter { it.attributes.containsKey("videoId") }
    .forEach {
        println(it.attributes["title"])
        println("duration:" + it.childElements.single().textContent)
    }
```