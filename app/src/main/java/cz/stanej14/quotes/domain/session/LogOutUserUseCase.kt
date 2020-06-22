package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.model.Resource

interface LogOutUserUseCase {
    suspend fun logOut(): Resource<Unit>
}