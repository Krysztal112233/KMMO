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

import dev.krysztal.kmmo.core.foundation.exception.registry.DuplicateRegistryObjectException
import dev.krysztal.kmmo.core.foundation.identifier.IdentifiedMark
import dev.krysztal.kmmo.core.foundation.identifier.Identifier
import it.unimi.dsi.fastutil.objects.ReferenceArrayList
import org.slf4j.LoggerFactory

/**
 * Registers and stores all objects that implement [IdentifiedMark].
 *
 * @see ReferenceArrayList
 * @see IdentifiedMark
 * @see Identifier
 * @author Krysztal112233
 */
abstract class AbstractRegistry<T : IdentifiedMark> {
    private val logger = LoggerFactory.getLogger(AbstractRegistry::class.java.simpleName)
    abstract val identifier: Identifier
    private val objects: ReferenceArrayList<T> = ReferenceArrayList();

    /**
     * Register an object implemented [IdentifiedMark].
     *
     * But objects with the same [Identifier] can only be registered once, otherwise an exception [DuplicateRegistryObjectException] is thrown.
     *
     * @throws DuplicateRegistryObjectException when trying register same [IdentifiedMark] object.
     */
    @Synchronized
    open fun register(valueObject: T): T {
        logger.info("Object [${valueObject.id}] is being registered to Registry ${this.identifier}.")
        if (valueObject is RegisterInvoke) {
            logger.debug("Call beforeRegisterInvoke() of object [{}].", valueObject.id)
            valueObject.beforeRegisterInvoke()
        }

        if (this.get(valueObject.id) != null) throw DuplicateRegistryObjectException(this, valueObject)
        logger.info("Registered object [${valueObject.id}] into Registry [${this.identifier}].")
        objects.push(valueObject)

        if (valueObject is RegisterInvoke) {
            logger.debug("Call afterRegisterInvoke() of object [{}].", valueObject.id)
            valueObject.afterRegisterInvoke()
        }
        return valueObject
    }

    /**
     * Collect all registered objects' [Identifier].
     *
     * @see [Identifier]
     */
    fun elements(): List<Identifier> = objects.map { it.id }.toList()

    /**
     * Deregister an object.
     *
     * @return Successful removal or not.
     */
    @Synchronized
    open fun deregister(identifier: Identifier): Boolean {
        logger.info("Trying to deregister [$identifier] from Registry [${this.identifier}].")

        val find = objects.find { it.id == identifier }

        if (find != null) {
            if (find is DeregisterInvoke) {
                logger.debug("Call beforeDeregisterInvoke() of object {}.", identifier)
                find.beforeDeregisterInvoke()
            }

            logger.info("Deregistered object [$identifier] of Registry [${this.identifier}]")
            objects.removeIf { it.id == identifier }

            if (find is DeregisterInvoke) {
                logger.debug("Call beforeDeregisterInvoke() of object {}.", identifier)
                find.afterDeregisterInvoke()
            }
        } else logger.warn("Cannot detect object with identifier [$identifier]. Deregister failed.")
        return find == null
    }

    /**
     * A wrapped method of [deregister], but param use [Identifier] type.
     *
     * @see deregister
     */
    fun deregister(identifiedMark: IdentifiedMark): Boolean = deregister(identifiedMark.id)

    /**
     * A wrapped method of [deregister], but param use [String] type.
     *
     * @see deregister
     */
    fun deregister(string: String): Boolean = deregister(Identifier.from(string))


    /**
     * Finds objects that match the [Identifier].
     *
     * @return When no object with a value equal to the Identifier is found, null is returned.
     * @see Identifier
     */
    @Synchronized
    fun get(identifier: Identifier): T? = objects.find { it.id == identifier }

    /**
     * A wrapped method of [get], but param use [String] type.
     *
     * @see get
     */
    fun get(string: String): T? = get(Identifier.from(string))

    /**
     * A wrapped method of [get], but param use [IdentifiedMark] type.
     *
     * @see get
     */
    fun get(identifiedMark: IdentifiedMark): T? = get(identifiedMark.id)
}