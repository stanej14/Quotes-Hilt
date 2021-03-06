package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.model.User
import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    fun storeUserInfo(user: User, token: String)
    fun getUserToken(): String?
    fun observeUserInfo(): Flow<User?>
    fun logout()
}