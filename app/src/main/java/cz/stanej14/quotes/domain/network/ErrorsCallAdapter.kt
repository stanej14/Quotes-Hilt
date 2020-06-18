package cz.stanej14.quotes.domain.network

import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Retrofit CallAdapter to wrap Call<> network errors in [ApiError].
 */
internal class ErrorsCallAdapter(
    private val delegate: CallAdapter<Any, Call<*>>,
    private val apiErrorsMapper: ApiErrorsMapper
) : CallAdapter<Any, Call<*>> by delegate {

    override fun adapt(call: Call<Any>): Call<*> =
        delegate.adapt(object : Call<Any> by call {

            /**
             * Wraps error in our custom [ApiError] or send it to the next adapter to be handled.
             */
            override fun enqueue(callback: Callback<Any>) {
                call.enqueue(object : Callback<Any> {

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            callback.onResponse(call, response)
                        } else {
                            callback.onFailure(call, apiErrorsMapper.mapErrorFromResponse(response))
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        callback.onFailure(call, apiErrorsMapper.mapError(t))
                    }
                })
            }
        })
}

/**
 * Factory to create our ErrorsCallAdapter if the request is the one we expect.
 */
class ErrorsCallAdapterFactory(private val apiErrorsMapper: ApiErrorsMapper) :
    CallAdapter.Factory() {

    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // We can handle Call<Foo> only which are coroutines suspend functions.
        val rawType = getRawType(returnType)
        val isCall = rawType == Call::class.java

        if (!isCall || returnType !is ParameterizedType) return null

        if (isCall) {
            val delegate = retrofit.nextCallAdapter(this, returnType, annotations)
            return ErrorsCallAdapter(delegate as CallAdapter<Any, Call<*>>, apiErrorsMapper)
        }
        return null
    }
}