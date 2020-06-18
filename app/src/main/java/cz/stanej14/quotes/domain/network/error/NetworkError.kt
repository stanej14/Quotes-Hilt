package cz.stanej14.quotes.domain.network.error

/**
 * Exception representing network error.
 */
class NetworkError(exception: Throwable) : ApiError(exception = exception)