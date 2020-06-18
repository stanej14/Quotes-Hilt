package cz.stanej14.quotes.domain.network.error

import retrofit2.HttpException
import retrofit2.Response

/**
 * Base class representing an error coming from the network call.
 */
open class ApiError(
    val errorCode: Int = 0,
    val exception: Throwable
) : RuntimeException() {

    constructor(response: Response<*>) : this(
        errorCode = response.code(),
        exception = HttpException(response)
    )

    override fun toString(): String {
        val error = (exception as? HttpException)?.message() ?: exception.toString()
        return "${this.javaClass.name}(errorCode=$errorCode, exception=$error)"
    }
}