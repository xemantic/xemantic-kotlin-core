/*
 * Copyright 2025 Kazimierz Pogoda / Xemantic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xemantic.kotlin.core

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A resource that can be closed or released using suspend functions.
 *
 * This is a `suspend` variant of [AutoCloseable] for resources that require
 * asynchronous cleanup operations, such as network connections, async file handles,
 * or coroutine-based resources.
 *
 * @see use
 */
public interface SuspendCloseable {

    /**
     * Closes this resource.
     *
     * This function may throw, thus it is strongly recommended to use the [use] function instead,
     * which closes this resource correctly whether an exception is thrown or not.
     *
     * Implementers of this interface should pay increased attention to cases where the close
     * operation may fail. It is recommended that all underlying resources are closed and the
     * resource internally is marked as closed before throwing an exception.
     *
     * Note that calling this function more than once may have some visible side effect.
     * However, implementers of this interface are strongly recommended to make this function idempotent.
     */
    public suspend fun close()

}

/**
 * Executes the given [block] function on this resource and then closes it down correctly
 * whether an exception is thrown or not.
 *
 * This is a `suspend` variant of [AutoCloseable.use] for resources implementing [SuspendCloseable].
 *
 * In case if the resource is being closed due to an exception occurred in [block], and
 * the closing also fails with an exception, the latter is added to the
 * [suppressed][Throwable.addSuppressed] exceptions of the former.
 *
 * @param block a suspending function to process this [SuspendCloseable] resource.
 * @return the result of [block] function invoked on this resource.
 */
public suspend inline fun <T : SuspendCloseable?, R> T.use(
    block: suspend (T) -> R
): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    var exception: Throwable? = null
    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        this.closeFinally(exception)
    }
}

/**
 * Closes this resource, handling exceptions according to the [cause].
 *
 * This is an internal helper function used by [use] to properly handle resource cleanup
 * with correct exception suppression semantics.
 *
 * @param cause the exception that occurred in the try block, or `null` if no exception occurred.
 */
@PublishedApi
internal suspend fun SuspendCloseable?.closeFinally(
    cause: Throwable?
): Unit = when {
    this == null -> {}
    cause == null -> close()
    else -> try {
        close()
    } catch (closeException: Throwable) {
        cause.addSuppressed(closeException)
    }
}
