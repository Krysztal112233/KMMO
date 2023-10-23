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
import dev.krysztal.kmmo.core.foundation.exception.persistence.RedisConnectionException
import dev.krysztal.kmmo.core.foundation.persistence.PersistenceProvider
import dev.krysztal.kmmo.core.gson
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

/**
 * @author Krysztal112233
 * @throws RedisConnectionException when unable to connect to Redis
 */
class RedisProvider(
    configuration: Map<String, String>,
    cacheBuilder: () -> KSimpleCache.Builder<String, Any>,
) : PersistenceProvider() {
    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)
    private var redis: KredsClient
    override val cache: KSimpleCache<String, Any> = cacheBuilder()
        .also {
            it.removeListener = { k, v, _ ->
                runBlocking { this@RedisProvider.putJson(k!!, gson.toJson(v)) }
            }
        }
        .build()

    override suspend fun getJson(key: String): String? = this.redis.get(key)

    override suspend fun putJson(key: String, json: String) {
        this.redis.set(key, json)
    }

    override suspend fun <T : Any> put(key: String, data: T) {
        this.cache.set(key, data)
    }

    override fun close() {
        this.sync()
        this.redis.close()
    }

    override fun sync() = this.cache.expelAll()

    init {
        logger.debug("Connecting to Redis ...")
        this.redis = newClient(Endpoint.from(configuration["endpoint"]!!))

        val pong = runBlocking { this@RedisProvider.redis.ping() }

        if (pong == null) {
            logger.error("Connection failed.")
            throw RedisConnectionException()
        }
        logger.debug("Get `PONG` from Redis: $pong.")
        logger.debug("The Redis connection was successful!")
    }

}