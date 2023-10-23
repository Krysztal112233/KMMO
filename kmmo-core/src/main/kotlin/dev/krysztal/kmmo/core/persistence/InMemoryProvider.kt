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

import com.google.gson.reflect.TypeToken
import dev.krysztal.kmmo.core.foundation.cache.KSimpleCache
import dev.krysztal.kmmo.core.foundation.cache.get
import dev.krysztal.kmmo.core.foundation.cache.set
import dev.krysztal.kmmo.core.foundation.persistence.PersistenceProvider
import dev.krysztal.kmmo.core.gson
import dev.krysztal.kmmo.core.pluginInstance
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.io.path.*

/**
 * @author Krysztal112233
 */
@Suppress("UNCHECKED_CAST")
class InMemoryProvider(
    configuration: Map<String, String>,
    cacheBuilder: () -> KSimpleCache.Builder<String, Any>,
) : PersistenceProvider() {
    private var memoryStorage: MutableMap<String, String> = ConcurrentSkipListMap()
    private var storagePath: Path
    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)
    override val cache: KSimpleCache<String, Any> = cacheBuilder()
        .also {
            it.removeListener = { k, v, _ ->
                runBlocking { this@InMemoryProvider.putJson(k!!, gson.toJson(v)) }
            }
        }
        .build()

    override suspend fun <T> get(key: String, typeToken: TypeToken<T>): T? where T : Any? {
        // TODO: Make it type safe.
        return this.cache[key] as T ?: super.get(key, typeToken)
    }

    override suspend fun <T> put(key: String, data: T) where T : Any {
        this.cache[key] = data
    }

    override suspend fun getJson(key: String): String? = this.memoryStorage[key]

    override suspend fun putJson(key: String, json: String) {
        this.memoryStorage[key] = json
    }

    init {
        this.storagePath = Path(
            if (pluginInstance == null) "./" else pluginInstance!!.dataFolder.toString(),
            configuration["file"] ?: "storage.json",
        )

        if (!this.storagePath.exists()) {
            logger.debug("Creating storage.json...")
            this.storagePath.createFile()
        } else {
            logger.debug("Loading storage.json...")
            val content = this.storagePath.readText()
            val t = gson.fromJson(
                content,
                ConcurrentSkipListMap::class.java,
            )
            this.memoryStorage = if (t != null) t as MutableMap<String, String> else mutableMapOf()
        }
    }

    override fun sync() {
        this.cache.expelAll()

        if (!this.storagePath.exists()) this.storagePath.createFile()
        val content = gson.toJson(this.memoryStorage)
        this.storagePath.writeText(content)
    }

    override fun close() {
        this.sync()
    }
}
