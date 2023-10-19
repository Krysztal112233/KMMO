package dev.krysztal.kmmo.core.persistence

import dev.krysztal.kmmo.core.foundation.persistence.get
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File

class InMemoryProviderTest {
    @BeforeEach
    fun prepare() {
        File("./storage.json").delete()
    }


    @Test
    @DisplayName("testBasicFunction")
    fun test() = runTest {
        InMemoryProvider.init(mapOf())
        InMemoryProvider.put("test.1", 1)

        assertEquals(InMemoryProvider.get("test.1") as Int?, 1)
    }

    @Test
    @DisplayName("testComplexObject")
    fun testComplexObject() = runTest {
        InMemoryProvider.init(mapOf())

        InMemoryProvider.put("test.1", TestComplex(1, TestComplex(2, null)))
    }
}


data class TestComplex(
    val v1: Int,
    val v2: TestComplex?,
)