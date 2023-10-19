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

package dev.krysztal.kmmo.core.foundation.registry

import dev.krysztal.kmmo.core.foundation.exception.registry.RegistryFrozenException
import dev.krysztal.kmmo.core.foundation.identifier.IdentifiedMark
import dev.krysztal.kmmo.core.foundation.identifier.Identifier

/**
 * [FreezableRegistry] will disable the registration of new content after all content has been registered.
 *
 * This is a new [AbstractRegistry] type for stability and runtime content known and debuggable reasons.
 *
 * I would recommend you to use this [AbstractRegistry] implementation over [DefaultRegistry].
 *
 * @see AbstractRegistry
 * @see DefaultRegistry
 * @author Krysztal112233
 */
class FreezableRegistry<T : IdentifiedMark>(
    override val identifier: Identifier
) : AbstractRegistry<T>() {
    /**
     * Get whether this [FreezableRegistry] is frozen.
     */
    @Volatile
    var isFrozen: Boolean = false
        private set

    /**
     *  Freeze this [FreezableRegistry].
     *
     *  This method will make this [FreezableRegistry] immutable.
     *
     *  @see unfreeze
     */
    fun freeze() {
        isFrozen = true
    }

    /**
     * Unfreeze this [FreezableRegistry]
     *
     * This method will make this [FreezableRegistry] immutable, **but will clear all registered object also!**
     *
     * @see freeze
     */
    fun unfreeze() {
        isFrozen = false
        this.elements().forEach(this::deregister)
    }

    /**
     * Registers and returns the registered value if the [FreezableRegistry] is not frozen.
     *
     * @throws RegistryFrozenException if the [AbstractRegistry] has been frozen but attempts to change the contents.
     * @see [AbstractRegistry.register]
     */
    override fun register(valueObject: T): T {
        if (isFrozen) throw RegistryFrozenException(this)
        return super.register(valueObject)
    }

    /**
     * Deregister a value when the [AbstractRegistry] is not frozen.
     *
     * @throws RegistryFrozenException if the [AbstractRegistry] has been frozen but attempts to change the contents.
     * @see [AbstractRegistry.deregister]
     */
    override fun deregister(identifier: Identifier): Boolean {
        if (isFrozen) throw RegistryFrozenException(this)
        return super.deregister(identifier)
    }
}