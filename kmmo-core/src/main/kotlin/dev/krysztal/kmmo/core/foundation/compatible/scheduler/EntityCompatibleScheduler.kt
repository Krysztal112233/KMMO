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

package dev.krysztal.kmmo.core.foundation.compatible.scheduler

import dev.krysztal.kmmo.core.foundation.compatible.runningFolia
import dev.krysztal.kmmo.core.pluginInstance
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * @author Krysztal112233
 */
class EntityCompatibleScheduler private constructor(builder: Builder) {
    private var entity: Entity = builder.entity
    private var plugin: JavaPlugin = pluginInstance!!
    private var task: (Entity) -> Unit = { Unit }
    private var retire: (Entity) -> Unit = { Unit }
    private var delay: Long = 0
    private var period: Long = 0

    fun register() {
        if (runningFolia()) this.foliaEntityScheduler()
        else this.spigotGlobalScheduler()
    }

    private fun foliaEntityScheduler() {
        if (period != 0L) {
            this.entity
                .scheduler
                .runAtFixedRate(plugin, { task.invoke(entity) }, { retire.invoke(entity) }, delay, period)
        } else {
            this.entity
                .scheduler
                .runDelayed(plugin, { task.invoke(entity) }, { retire.invoke(entity) }, delay)
        }
    }

    private fun spigotGlobalScheduler() {
        val f: (BukkitTask) -> Unit = {
            val targetEntity = Bukkit.getEntity(this.entity.uniqueId)
            if (targetEntity != null) task.invoke(targetEntity)
            else retire.invoke(entity)
        }

        Bukkit.getScheduler().runTaskTimer(plugin, f, delay, period)
    }

    class Builder(val entity: Entity) {
        var plugin: JavaPlugin = pluginInstance!!
        var task: (Entity) -> Unit = { Unit }
        var retire: (Entity) -> Unit = { Unit }
        var delay: Long = 0
        var period: Long = 0

        fun build(): EntityCompatibleScheduler = EntityCompatibleScheduler(this)
    }
}