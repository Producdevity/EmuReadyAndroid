package com.emuready.emuready.domain.exceptions

sealed class EmuReadyException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class NetworkException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class AuthException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class EmulatorException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class ValidationException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)

class ApiException(
    message: String,
    cause: Throwable? = null
) : EmuReadyException(message, cause)