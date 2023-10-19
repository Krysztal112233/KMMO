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

import dev.krysztal.kmmo.core.gson

/**
 * In the development, we will find that many times we need to persist some data ,
 * and by different scales will need to use different persistence schemes ,
 * so the PersistenceProvider interface is provided for the implementation of the persistence provider.
 *
 * @author Krysztal112233
 */
interface PersistenceProvider {
    suspend fun getJson(key: String): String?

    suspend fun <T> put(key: String, data: T) {
        this.putJson(key, gson.toJson(data))
    }

    suspend fun putJson(key: String, json: String)

    suspend fun init(configuration: Map<String, String>)

    fun close() {}

    fun sync() {}
}

/**
 * @author Krysztal112233
 */
suspend inline fun <reified T> PersistenceProvider.get(key: String): T? {
    return gson.fromJson(getJson(key) ?: return null, T::class.java)
}