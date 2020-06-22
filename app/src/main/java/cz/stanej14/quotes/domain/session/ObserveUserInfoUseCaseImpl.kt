package cz.stanej14.quotes.domain.session

import javax.inject.Inject

class ObserveUserInfoUseCaseImpl @Inject constructor(private val userSessionRepository: UserSessionRepository) :
    ObserveUserInfoUseCase {

    override fun observeUserInfo() = userSessionRepository.observeUserInfo()
}