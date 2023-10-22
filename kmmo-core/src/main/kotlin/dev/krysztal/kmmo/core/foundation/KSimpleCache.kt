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

package dev.krysztal.kmmo.core.foundation

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalCause
import dev.krysztal.kmmo.core.foundation.identifier.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Krysztal112233
 */
class KSimpleCache<K, V> private constructor(builder: Builder<K, V>)
        where K : Any,
              V : Any {

    private val identifier: Identifier? = builder.identifier
    private val logger: Logger = builder.logger
    private val cache: Cache<K, V> = Caffeine
        .from(builder.spec)
        .removalListener<K, V> { k, v, c -> builder.removeListener(k, v, c) }
        .build()

    private val statsLogger: () -> Unit = run {
        if (builder.enableStats) {
            { logger.debug("Cache \"{}\" stats: {}", this.identifier, this.cache.stats()) }
        } else {
            {}
        }
    }

    fun put(k: K, v: V) {
        this.cache.put(k, v)
        this.logStats()
    }

    fun get(k: K): V? {
        val v = this.cache.getIfPresent(k)
        this.logStats()
        return v
    }

    private fun logStats() = this.statsLogger()

    class Builder<K, V>
            where K : Any,
                  V : Any {
        var spec: String = "maximumSize=10000, expireAfterWrite=10m"
        var removeListener: (k: K?, v: V?, c: RemovalCause) -> Unit = { _, _, _ -> }
        var logger: Logger = LoggerFactory.getLogger(KSimpleCache::class.java.simpleName)
        var identifier: Identifier? = null
        var enableStats: Boolean = true

        fun build(): KSimpleCache<K, V> = KSimpleCache(this)
    }
}