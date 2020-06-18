package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.model.User

interface UserSessionRepository {
    fun storeUserInfo(user: User, token: String)
    fun getUserToken(): String?
}