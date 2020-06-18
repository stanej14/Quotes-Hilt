package cz.stanej14.quotes.domain.network.error

/**
 * Exception representing unexpected error while processing the API response.
 */
class UnexpectedError(errorCode: Int = 0, exception: Throwable) :
    ApiError(errorCode = errorCode, exception = exception)