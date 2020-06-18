package cz.stanej14.quotes.domain.session

interface HasUserSessionUseCase {
    fun hasUserSession(): Boolean
}