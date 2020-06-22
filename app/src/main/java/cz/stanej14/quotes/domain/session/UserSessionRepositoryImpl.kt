package cz.stanej14.quotes.domain.session

import android.content.SharedPreferences
import androidx.core.content.edit
import cz.stanej14.quotes.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.log

@ExperimentalCoroutinesApi
@Singleton
class UserSessionRepositoryImpl @Inject constructor(@Named("secured_session") private val sharedPreferences: SharedPreferences) :
    UserSessionRepository {

    companion object {
        private const val KEY_LOGIN = "login"
        private const val KEY_EMAIL = "email"
        private const val KEY_TOKEN = "token"
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == KEY_LOGIN || key == KEY_EMAIL) {
            collectUserInfo()
        }
    }
    private val userChannel = ConflatedBroadcastChannel<User?>()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
        collectUserInfo()
    }

    private fun collectUserInfo() {
        val login = sharedPreferences.getString(KEY_LOGIN, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        if (login != null && email != null) {
            userChannel.offer(User(login, email))
        } else {
            userChannel.offer(null)
        }
    }

    override fun storeUserInfo(user: User, token: String) {
        sharedPreferences.edit {
            putString(KEY_LOGIN, user.login)
            putString(KEY_EMAIL, user.email)
            putString(KEY_TOKEN, token)
        }
    }

    override fun getUserToken() = sharedPreferences.getString(KEY_TOKEN, null)

    override fun observeUserInfo(): Flow<User?> = userChannel.asFlow()

    override fun logout() {
        sharedPreferences.edit { clear() }
        userChannel.offer(null)
    }
}