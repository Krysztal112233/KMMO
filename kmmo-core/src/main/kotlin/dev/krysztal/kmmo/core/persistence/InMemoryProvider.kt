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

import com.google.gson.Gson
import dev.krysztal.kmmo.core.foundation.persistence.PersistenceProvider
import dev.krysztal.kmmo.core.gson
import dev.krysztal.kmmo.core.pluginInstance
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.concurrent.ConcurrentSkipListMap
import kotlin.io.path.*

/**
 * @author Krysztal112233
 */
object InMemoryProvider : PersistenceProvider {
    private var memoryStorage: MutableMap<String, String> = ConcurrentSkipListMap()
    private lateinit var storagePath: Path
    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    override suspend fun getJson(key: String): String? = this.memoryStorage[key]

    override suspend fun putJson(key: String, json: String) {
        this.memoryStorage[key] = json
    }

    override suspend fun init(configuration: Map<String, String>) {

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
            ) ?: return
            this.memoryStorage = t as MutableMap<String, String>
        }
    }

    override fun sync() {
        if (!this.storagePath.exists()) this.storagePath.createFile()
        val content = gson.toJson(this.memoryStorage)
        this.storagePath.writeText(content)
    }

    override fun close() {
        this.sync()
    }
}
