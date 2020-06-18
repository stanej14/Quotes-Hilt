package cz.stanej14.quotes.di

import cz.stanej14.quotes.domain.favorite.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.domain.favorite.ToggleFavoriteQuoteUseCaseImpl
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCase
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCaseImpl
import cz.stanej14.quotes.domain.landing.ObtainLandingQuoteUseCase
import cz.stanej14.quotes.domain.landing.ObtainLandingQuoteUseCaseImpl
import cz.stanej14.quotes.domain.session.HasUserSessionUseCase
import cz.stanej14.quotes.domain.session.HasUserSessionUseCaseImpl
import cz.stanej14.quotes.domain.session.LoginUserUseCase
import cz.stanej14.quotes.domain.session.LoginUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindObtainLandingQuoteUseCase(impl: ObtainLandingQuoteUseCaseImpl): ObtainLandingQuoteUseCase

    @Binds
    abstract fun bindObtainQuotesUseCase(impl: ObtainQuotesUseCaseImpl): ObtainQuotesUseCase

    @Binds
    abstract fun bindLoginUserUseCase(impl: LoginUserUseCaseImpl): LoginUserUseCase

    @Binds
    abstract fun bindToggleFavoriteQuoteUseCase(impl: ToggleFavoriteQuoteUseCaseImpl): ToggleFavoriteQuoteUseCase

    @Binds
    abstract fun bindHasUserSessionUseCase(impl: HasUserSessionUseCaseImpl): HasUserSessionUseCase
}
