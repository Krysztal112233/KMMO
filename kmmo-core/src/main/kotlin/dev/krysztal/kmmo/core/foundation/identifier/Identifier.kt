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

package dev.krysztal.kmmo.core.foundation.identifier

import dev.krysztal.kmmo.core.foundation.exception.format.InvalidStringFormatException

/**
 * Unique values that apply to the [IdentifiedMark] interface.
 *
 * @author Krysztal112233
 * @see IdentifiedMark
 */
data class Identifier(
    val namespace: String = "kmmo",
    val path: String,
) {

    override fun toString(): String = "$namespace:$path"

    companion object {
        /**
         * Construct new [Identifier] from input string.
         *
         *  @throws InvalidStringFormatException if input is invalid format.
         */
        fun from(identifier: String): Identifier {
            val split = identifier.split(":")
            if (split.size != 2) {
                throw InvalidStringFormatException("Identifier $identifier do not fulfill the format %s:%s.")
            }
            return Identifier(split[0], split[1])
        }

        /**
         * Construct new [Identifier] from object which implemented [IdentifiedMark] interface.
         *
         * @see IdentifiedMark
         */
        fun from(identifier: IdentifiedMark): Identifier = identifier.id
    }
}