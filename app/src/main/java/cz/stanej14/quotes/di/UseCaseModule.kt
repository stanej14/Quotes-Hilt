package cz.stanej14.quotes.di

import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCase
import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCaseImpl
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCaseImpl
import cz.stanej14.quotes.domain.feed.ObserveQuotesUseCase
import cz.stanej14.quotes.domain.feed.ObserveQuotesUseCaseImpl
import cz.stanej14.quotes.domain.landing.ObserveLandingQuoteUseCase
import cz.stanej14.quotes.domain.landing.ObserveLandingQuoteUseCaseImpl
import cz.stanej14.quotes.domain.session.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindObtainLandingQuoteUseCase(impl: ObserveLandingQuoteUseCaseImpl): ObserveLandingQuoteUseCase

    @Binds
    abstract fun bindObtainQuotesUseCase(impl: ObserveQuotesUseCaseImpl): ObserveQuotesUseCase

    @Binds
    abstract fun bindLoginUserUseCase(impl: LoginUserUseCaseImpl): LoginUserUseCase

    @Binds
    abstract fun bindToggleFavoriteQuoteUseCase(impl: ToggleFavoriteQuoteUseCaseImpl): ToggleFavoriteQuoteUseCase

    @Binds
    abstract fun bindHasUserSessionUseCase(impl: HasUserSessionUseCaseImpl): HasUserSessionUseCase

    @Binds
    abstract fun bindObserveQuoteUseCase(impl: ObserveQuoteUseCaseImpl): ObserveQuoteUseCase

    @Binds
    abstract fun bindObserveUserInfoUseCase(impl: ObserveUserInfoUseCaseImpl): ObserveUserInfoUseCase

    @Binds
    abstract fun bindLogOutUserUseCase(impl: LogOutUserUseCaseImpl): LogOutUserUseCase
}
