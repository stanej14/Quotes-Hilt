package cz.stanej14.quotes.domain.session

import android.content.SharedPreferences
import androidx.core.content.edit
import cz.stanej14.quotes.model.User
import javax.inject.Inject
import javax.inject.Named

class UserSessionRepositoryImpl @Inject constructor(@Named("secured_session") private val sharedPreferences: SharedPreferences) :
    UserSessionRepository {

    companion object {
        private const val KEY_LOGIN = "login"
        private const val KEY_EMAIL = "email"
        private const val KEY_TOKEN = "token"
    }

    override fun storeUserInfo(user: User, token: String) {
        sharedPreferences.edit {
            putString(KEY_LOGIN, user.login)
            putString(KEY_EMAIL, user.email)
            putString(KEY_TOKEN, token)
        }
    }

    override fun getUserToken() = sharedPreferences.getString(KEY_TOKEN, null)
}