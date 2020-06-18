package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.model.Resource

interface LoginUserUseCase {
    suspend fun login(login: String, password: String): Resource<Unit>
}