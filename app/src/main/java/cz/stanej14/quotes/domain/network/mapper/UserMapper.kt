package cz.stanej14.quotes.domain.network.mapper

import cz.stanej14.quotes.domain.network.session.SessionResponse
import cz.stanej14.quotes.model.User

class UserMapper {

    fun map(response: SessionResponse): User {
        return User(
            login = response.login,
            email = response.email
        )
    }
}