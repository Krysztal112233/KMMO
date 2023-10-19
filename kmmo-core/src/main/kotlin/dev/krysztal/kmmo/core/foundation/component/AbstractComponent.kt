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

package dev.krysztal.kmmo.core.foundation.component

import dev.krysztal.kmmo.core.foundation.identifier.Identifier
import dev.krysztal.kmmo.core.foundation.exception.ComponentNotFoundException

/**
 * Attach any data to any object.
 *
 * ## Design
 *
 * Developers need to use custom implementations to achieve access to data.
 *
 * Suppose you need to attach a component to an object, say an Item object,
 * then you need to implement your own component type, which we'll assume is component _A_ here.
 *
 * Component _A_ needs to have default values set,
 * and _A_ may need to choose the right time to attach to the object it wants to attach to.
 *
 * As an example, you could choose to attach component _A_ to an item when the player picks it up,
 * so that the item has component A attached to it.
 *
 * Components will be accessible normally when they are available.
 *
 * @param T Attach target type.
 * @param E Attach data type.
 * @author Krysztal112233
 */
abstract class AbstractComponent<T, E>(
    val identifier: Identifier,
) {

    /**
     * @throws ComponentNotFoundException When the component is not available
     */
    abstract fun get(target: T): E

    abstract fun set(target: T, data: E)

    abstract fun has(target: T): Boolean

    open fun default(target: T) {}
}