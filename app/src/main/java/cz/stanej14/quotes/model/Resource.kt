package cz.stanej14.quotes.model

import cz.stanej14.quotes.model.Resource.Success

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

val <T> Resource<T>.data: T?
    get() = (this as? Success)?.data
