package cz.stanej14.quotes.domain.network

import cz.stanej14.quotes.domain.network.error.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

class ApiErrorsMapper {

    fun mapErrorFromResponse(response: Response<*>): ApiError {
        val statusCode = response.code()
        val exception = HttpException(response)

        return when (statusCode) {
            HttpURLConnection.HTTP_BAD_REQUEST -> BadRequestError(exception)
            HttpURLConnection.HTTP_NOT_FOUND -> NotFoundError(exception)
            in 500..599 -> ServerError(statusCode, exception)
            else -> UnexpectedError(statusCode, exception)
        }
    }

    fun mapError(throwable: Throwable): ApiError {
        return when (throwable) {
            is ApiError -> throwable
            is HttpException -> {
                throwable.response()?.let { mapErrorFromResponse(it) }
                    ?: UnexpectedError(exception = throwable)
            }
            is SocketTimeoutException -> NetworkError(throwable)
            is IOException -> NetworkError(throwable)
            else -> UnexpectedError(exception = throwable)
        }
    }
}