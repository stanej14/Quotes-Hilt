package cz.stanej14.quotes.domain.network.error

/**
 * Exception for server errors (5xx).
 */
class ServerError(errorCode: Int, exception: Throwable) :
    ApiError(errorCode = errorCode, exception = exception)