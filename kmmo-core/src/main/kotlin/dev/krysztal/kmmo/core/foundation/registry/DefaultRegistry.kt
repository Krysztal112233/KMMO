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

import dev.krysztal.kmmo.core.foundation.identifier.IdentifiedMark
import dev.krysztal.kmmo.core.foundation.identifier.Identifier

/**
 * Default [AbstractRegistry] implementation.
 *
 * I don't recommend you to use this because [AbstractRegistry] without freezing
 * mechanism causes the content to become **uncontrollable**.
 *
 * @see AbstractRegistry
 * @see FreezableRegistry
 * @author Krysztal112233
 */
class DefaultRegistry<T : IdentifiedMark>(
    override val identifier: Identifier,
) : AbstractRegistry<T>()