package cz.stanej14.quotes.di

import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCase
import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCaseImpl
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCaseImpl
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCase
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCaseImpl
import cz.stanej14.quotes.domain.landing.ObserveLandingQuoteUseCase
import cz.stanej14.quotes.domain.landing.ObserveLandingQuoteUseCaseImpl
import cz.stanej14.quotes.domain.session.HasUserSessionUseCase
import cz.stanej14.quotes.domain.session.HasUserSessionUseCaseImpl
import cz.stanej14.quotes.domain.session.LoginUserUseCase
import cz.stanej14.quotes.domain.session.LoginUserUseCaseImpl
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
    abstract fun bindObtainQuotesUseCase(impl: ObtainQuotesUseCaseImpl): ObtainQuotesUseCase

    @Binds
    abstract fun bindLoginUserUseCase(impl: LoginUserUseCaseImpl): LoginUserUseCase

    @Binds
    abstract fun bindToggleFavoriteQuoteUseCase(impl: ToggleFavoriteQuoteUseCaseImpl): ToggleFavoriteQuoteUseCase

    @Binds
    abstract fun bindHasUserSessionUseCase(impl: HasUserSessionUseCaseImpl): HasUserSessionUseCase

    @Binds
    abstract fun bindObserveQuoteUseCase(impl: ObserveQuoteUseCaseImpl): ObserveQuoteUseCase
}
