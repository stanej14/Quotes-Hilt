package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class LogOutUserUseCaseImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val userSessionRepository: UserSessionRepository
) : LogOutUserUseCase {

    override suspend fun logOut(): Resource<Unit> {
        return try {
            quotesService.deleteUserSession()
            userSessionRepository.logout()
            Resource.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}