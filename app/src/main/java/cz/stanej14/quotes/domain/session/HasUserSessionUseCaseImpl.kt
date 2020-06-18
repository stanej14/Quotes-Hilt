package cz.stanej14.quotes.domain.session

import javax.inject.Inject

class HasUserSessionUseCaseImpl @Inject constructor(private val userSessionRepository: UserSessionRepository) :
    HasUserSessionUseCase {
    override fun hasUserSession() = userSessionRepository.getUserToken() != null
}