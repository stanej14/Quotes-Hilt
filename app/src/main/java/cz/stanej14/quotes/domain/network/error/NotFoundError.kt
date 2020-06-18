package cz.stanej14.quotes.domain.network.error

import java.net.HttpURLConnection

/**
 * Exception for Not Found (404).
 */
class NotFoundError(exception: Throwable) :
    ApiError(errorCode = HttpURLConnection.HTTP_NOT_FOUND, exception = exception)
