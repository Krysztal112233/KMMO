package dev.krysztal.kmmo.core.foundation.script.unit

import groovy.lang.GroovyShell
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class GroovyScriptUnitTest {
    private val groovy = GroovyShell()
    private val script =
        GroovyScriptUnit(groovy.parse(File(Thread.currentThread().contextClassLoader.getResource("test.groovy").file)))


    @Test
    @DisplayName("testInvokeMethod")
    fun testInvokeMethod() {
        assertEquals("1,2,3", this.script.invoke("entry", String::class.java, "1", "2", 3))
    }
}