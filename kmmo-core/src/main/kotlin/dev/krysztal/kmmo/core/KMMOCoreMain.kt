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

package dev.krysztal.kmmo.core

import dev.krysztal.kmmo.core.foundation.compatible.runningFolia
import dev.krysztal.kmmo.core.persistence.InMemoryProvider
import dev.krysztal.kmmo.core.persistence.RedisProvider
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin

class KMMOCoreMain() : JavaPlugin() {
    override fun onLoad() {
        if (!started) started = true
        else this.logger.warning("Please restart server instead of reload server!")

        if (!runningFolia()) {
            this.logger.warning("┌──────────────────────────────────────────────────────────────────────┐")
            this.logger.warning("│▄▄      ▄▄    ▄▄     ▄▄▄▄▄▄    ▄▄▄   ▄▄   ▄▄▄▄▄▄   ▄▄▄   ▄▄     ▄▄▄▄  │")
            this.logger.warning("│██      ██   ████    ██▀▀▀▀██  ███   ██   ▀▀██▀▀   ███   ██   ██▀▀▀▀█ │")
            this.logger.warning("│▀█▄ ██ ▄█▀   ████    ██    ██  ██▀█  ██     ██     ██▀█  ██  ██       │")
            this.logger.warning("│ ██ ██ ██   ██  ██   ███████   ██ ██ ██     ██     ██ ██ ██  ██  ▄▄▄▄ │")
            this.logger.warning("│ ███▀▀███   ██████   ██  ▀██▄  ██  █▄██     ██     ██  █▄██  ██  ▀▀██ │")
            this.logger.warning("│ ███  ███  ▄██  ██▄  ██    ██  ██   ███   ▄▄██▄▄   ██   ███   ██▄▄▄██ │")
            this.logger.warning("│ ▀▀▀  ▀▀▀  ▀▀    ▀▀  ▀▀    ▀▀▀ ▀▀   ▀▀▀   ▀▀▀▀▀▀   ▀▀   ▀▀▀     ▀▀▀▀  │")
            this.logger.warning("│──────────────────────────────────────────────────────────────────────│")
            this.logger.warning("│               Switch to Folia for the best experience!               │")
            this.logger.warning("│                See: https://papermc.io/software/folia                │")
            this.logger.warning("└──────────────────────────────────────────────────────────────────────┘")
        }

        KMMOConfig.reloadConfig(this)
        runBlocking { persistenceProviderSelect() }
    }

    override fun onDisable() {
    }

    private suspend fun persistenceProviderSelect() {
        when (KMMOConfig.persistenceType) {
            PersistenceProviderType.Redis -> {
                RedisProvider.init(
                    mapOf(
                        "endpoint" to this.config.getString("persistence.configuration.redis.endpoint")!!,
                    )
                )
                persistenceProvider = RedisProvider
            }

            PersistenceProviderType.InMemory -> {
                InMemoryProvider.init(
                    mapOf(
                        "file" to this.config.getString("persistence.configuration.memory.file")!!,
                    )
                )
                persistenceProvider = InMemoryProvider
            }
        }
    }

    companion object {
        private var started = false
    }
}