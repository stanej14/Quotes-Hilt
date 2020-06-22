package cz.stanej14.quotes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKeys
import cz.stanej14.quotes.data.QuotesRepository
import cz.stanej14.quotes.data.QuotesRepositoryImpl
import cz.stanej14.quotes.domain.error.ErrorHandler
import cz.stanej14.quotes.domain.network.mapper.QuoteMapper
import cz.stanej14.quotes.domain.network.mapper.UserMapper
import cz.stanej14.quotes.domain.session.UserSessionRepository
import cz.stanej14.quotes.domain.session.UserSessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DomainModule {

    @Provides
    fun provideQuoteMapper() = QuoteMapper()

    @Provides
    fun provideUserMapper() = UserMapper()

    @Provides
    fun provideErrorHandler() = ErrorHandler()

    @Provides
    @Singleton
    @Named("secured_session")
    fun provideSecuredSessionSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "encrypted_user_session",
            masterKeyAlias,
            context,
            AES256_SIV,
            AES256_GCM
        )
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@InstallIn(ApplicationComponent::class)
abstract class DomainModuleBinds {

    @Binds
    abstract fun bindUserSessionRepository(impl: UserSessionRepositoryImpl): UserSessionRepository

    @Binds
    abstract fun bindQuoteRepository(impl: QuotesRepositoryImpl): QuotesRepository
}