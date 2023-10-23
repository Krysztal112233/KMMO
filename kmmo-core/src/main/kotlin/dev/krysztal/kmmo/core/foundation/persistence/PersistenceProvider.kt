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

package dev.krysztal.kmmo.core.foundation.persistence

import com.google.gson.reflect.TypeToken
import dev.krysztal.kmmo.core.foundation.cache.KSimpleCache
import dev.krysztal.kmmo.core.foundation.extender.typeToken
import dev.krysztal.kmmo.core.gson
import dev.krysztal.kmmo.core.pluginScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * In the development, we will find that many times we need to persist some data ,
 * and by different scales will need to use different persistence schemes ,
 * so the PersistenceProvider interface is provided for the implementation of the persistence provider.
 *
 * @author Krysztal112233
 */
abstract class PersistenceProvider {
    internal abstract val cache: KSimpleCache<String, *>

    /**
     * To improve performance, the implementation of [PersistenceProvider]
     * should use [KSimpleCache] to accelerate **reading**.
     *
     * @see KSimpleCache
     */
    open suspend fun <T> get(key: String, typeToken: TypeToken<T>): T? {
        return gson.fromJson(this.getJson(key) ?: return null, typeToken)
    }

    /**
     * Don't invoke this method directly, it's slow and huge.
     *
     * It's the basis **reading** implementation of [PersistenceProvider]
     */
    abstract suspend fun getJson(key: String): String?

    /**
     * To improve performance, the implementation of [PersistenceProvider]
     * should use [KSimpleCache] to accelerate **writing**.
     *
     * @see KSimpleCache
     */
    open suspend fun <T> put(key: String, data: T) where  T : Any = this.putJson(key, gson.toJson(data))

    /**
     * Don't invoke this method directly, it's slow and huge.
     *
     * It's the basis **writing** implementation of [PersistenceProvider]
     */
    abstract suspend fun putJson(key: String, json: String)

    abstract fun close()

    /**
     * Sync all in-memory data (in cache or hashmap) into external persistence storage.
     */
    abstract fun sync()
}

public operator fun <T> PersistenceProvider.set(key: String, value: T)
        where T : Any = runBlocking { this@set.put(key, value) }

public inline operator fun <reified T> PersistenceProvider.get(key: String) where T : Any? =
    runBlocking { this@get.get(key, T::class.java.typeToken()) }


