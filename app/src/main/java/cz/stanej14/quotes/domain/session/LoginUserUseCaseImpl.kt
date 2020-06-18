package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.data.QuotesService
import cz.stanej14.quotes.domain.network.mapper.UserMapper
import cz.stanej14.quotes.domain.network.session.CreateSessionRequestBody
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import java.lang.Exception
import javax.inject.Inject

class LoginUserUseCaseImpl @Inject constructor(
    private val quotesService: QuotesService,
    private val userSessionRepository: UserSessionRepository,
    private val userMapper: UserMapper
) : LoginUserUseCase {

    override suspend fun login(login: String, password: String): Resource<Unit> {
        return try {
            val body = CreateSessionRequestBody(CreateSessionRequestBody.UserDto(login, password))
            val session = quotesService.createUserSession(body)
            val user = userMapper.map(session)
            userSessionRepository.storeUserInfo(user, session.token)
            Resource.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}