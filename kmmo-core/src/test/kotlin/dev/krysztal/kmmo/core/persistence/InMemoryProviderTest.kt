/*
 * Copyright (c) 2023 Krysztal112233
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.krysztal.kmmo.core.persistence

import dev.krysztal.kmmo.core.foundation.cache.KSimpleCache
import dev.krysztal.kmmo.core.foundation.extender.typeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
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
        val p = InMemoryProvider(mapOf()) { KSimpleCache.Builder<String, Any>() }

        p.put("test.1", 1)
        assertEquals(1, p.get("test.1", Int::class.java.typeToken()))
    }

    @Test
    @DisplayName("testComplexObject")
    fun testComplexObject() = runTest {
        val p = InMemoryProvider(mapOf()) { KSimpleCache.Builder<String, Any>() }
        p.put("test.1", TestComplex(1, TestComplex(2, null)))
        val t = async { p.get("test.1", TestComplex::class.java.typeToken()) }.await()

        assertEquals(1, t?.v1)
    }

    @AfterEach
    fun clean() {
        File("storage.json").delete()
    }
}


data class TestComplex(
    val v1: Int,
    val v2: TestComplex?,
)