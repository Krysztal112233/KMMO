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

package dev.krysztal.kmmo.core.foundation.component.item

import de.tr7zw.nbtapi.NBT
import dev.krysztal.kmmo.core.foundation.exception.ComponentNotFoundException
import dev.krysztal.kmmo.core.foundation.identifier.Identifier
import org.bukkit.inventory.ItemStack

/**
 * @author Krysztal112233
 */
class DoubleComponent(
    identifier: Identifier,
    private val default: Double,
) : AbstractItemComponent<Double>(identifier) {
    override fun get(target: ItemStack): Double {
        if (!this.has(target)) throw ComponentNotFoundException(target, this)

        return NBT.get<Double>(target) {
            it.getDouble(identifier.toString())
        }
    }

    override fun set(target: ItemStack, data: Double) {
        NBT.modify(target) { it.setDouble(identifier.toString(), data) }
    }

    override fun default(target: ItemStack) = this.set(target, this.default)
}