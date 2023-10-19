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

package dev.krysztal.kmmo.core.foundation.script.unit

import groovy.lang.Binding
import groovy.lang.Script
import org.slf4j.LoggerFactory

class GroovyScriptUnit(script: Script) : ScriptUnit<Script>(script) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun bind(name: String, obj: Any) {
        val binding = this.script.binding ?: Binding()
        binding.setVariable(name, obj)
        this.script.binding = binding
    }

    override fun <E> invoke(function: String, clazz: Class<E>?, vararg args: Any,): E? {
        return this.cast(clazz, logger) { this.script.invokeMethod(function, args) }
    }

    override fun <E> getProperty(name: String, clazz: Class<E>?): E? {
        return this.cast(clazz, logger) { this.script.getProperty(name) }
    }

    override fun <E> setProperty(name: String, obj: E) {
        this.script.binding.setVariable(name, obj)
    }
}