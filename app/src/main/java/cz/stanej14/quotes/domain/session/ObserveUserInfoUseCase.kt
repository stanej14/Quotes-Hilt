package cz.stanej14.quotes.domain.session

import cz.stanej14.quotes.model.User
import kotlinx.coroutines.flow.Flow

interface ObserveUserInfoUseCase {
    fun observeUserInfo(): Flow<User?>
}