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

import dev.krysztal.kmmo.core.foundation.script.unit.GroovyScriptUnit
import dev.krysztal.kmmo.core.foundation.script.unit.ScriptUnit
import groovy.lang.Script
import groovy.util.GroovyScriptEngine
import org.slf4j.LoggerFactory
import java.nio.file.Path

class GroovyEngine(scriptDirectory: Path) : ScriptEngine<GroovyScriptEngine, Script>(scriptDirectory) {
    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)
    override val engine: GroovyScriptEngine
        get() = GroovyScriptEngine(scriptDirectory.toString(), parentClassLoader)

    override fun script(script: String): ScriptUnit<Script>? {
        val result = this.engine.loadScriptByName(script).runCatching {
            this as Script
        }.onSuccess {
            this.logger.trace("Script path:{}", script)
            this.logger.debug("Get script instance from engine {}.", this.engine)
        }.onFailure {
            this.logger.error("Failed to get script.")
            it.printStackTrace()
        }.getOrNull()
        return GroovyScriptUnit(result ?: return null)
    }
}