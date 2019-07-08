package org.mibanez.instawalk.domain.error

/**
 * Domain errors will fit with one of these types.
 */
sealed class Error {
    object ServerError : Error()
}