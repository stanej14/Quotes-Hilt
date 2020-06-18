package cz.stanej14.quotes.domain.network.error

import java.net.HttpURLConnection

/**
 * Exception for Bad Request (400).
 */
class BadRequestError(exception: Throwable) :
    ApiError(HttpURLConnection.HTTP_BAD_REQUEST, exception)