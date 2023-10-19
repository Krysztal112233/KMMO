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

package dev.krysztal.kmmo.core.foundation.script.engine

import dev.krysztal.kmmo.core.foundation.script.unit.ScriptUnit
import java.nio.file.Path

/**
 * @author krysztal112233
 */
abstract class ScriptEngine<T, S>(
    val scriptDirectory: Path,
    val sha256: String? = null,
    val parentClassLoader: ClassLoader = Thread.currentThread().contextClassLoader
) {
    abstract val engine: T

    fun <E> invoke(script: String, function: String, clazz: Class<E>, vararg args: Any) =
        this.script(script)?.invoke(function, clazz, args)

    abstract fun script(script: String): ScriptUnit<S>?
}
