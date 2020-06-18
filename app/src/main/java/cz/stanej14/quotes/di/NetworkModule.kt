package cz.stanej14.quotes.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.stanej14.quotes.BuildConfig
import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.domain.network.ApiErrorsMapper
import cz.stanej14.quotes.domain.network.ErrorsCallAdapterFactory
import cz.stanej14.quotes.domain.session.UserSessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(userSessionRepository: UserSessionRepository): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(object : Authenticator {
                override fun authenticate(route: Route?, response: Response): Request {
                    val token = "Token token=\"${BuildConfig.API_TOKEN}\""
                    return response.request.newBuilder()
                        .addHeader("Authorization", token)
                        .also {
                            val userToken = userSessionRepository.getUserToken()
                            if (userToken != null) {
                                it.addHeader("User-Token", userToken)
                            }
                        }
                        .addHeader("Content-Type", "application/json")
                        .build()
                }
            })
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideErrorCallAdapterFactory(): ErrorsCallAdapterFactory {
        return ErrorsCallAdapterFactory(ApiErrorsMapper())
    }

    @Provides
    fun provideQuotesService(
        okHttpClient: OkHttpClient,
        errorsCallAdapterFactory: ErrorsCallAdapterFactory
    ): QuotesService {
        return Retrofit.Builder()
            .baseUrl("https://favqs.com/api/")
            .addCallAdapterFactory(errorsCallAdapterFactory)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(okHttpClient)
            .build()
            .create(QuotesService::class.java)
    }
}