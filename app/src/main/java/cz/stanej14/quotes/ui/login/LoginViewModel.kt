package cz.stanej14.quotes.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.session.LogOutUserUseCase
import cz.stanej14.quotes.domain.session.LoginUserUseCase
import cz.stanej14.quotes.domain.session.ObserveUserInfoUseCase
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val logOutUserUseCase: LogOutUserUseCase,
    private val observeUserInfoUseCase: ObserveUserInfoUseCase
) : ViewModel() {

    private val _login = MutableLiveData<Resource<Unit>>()
    val login: LiveData<Resource<Unit>> = _login

    private val _user = MutableLiveData<LoginState>()
    val user: LiveData<LoginState> = _user

    init {
        viewModelScope.launch {
            observeUserInfoUseCase.observeUserInfo().collect { user ->
                val state =
                    if (user != null) LoginState.UserLoggedIn(user) else LoginState.UserLoggedOut
                _user.postValue(state)
            }
        }
    }

    fun onCtaClicked(login: String, password: String) {
        _login.value = Resource.Loading
        try {
            viewModelScope.launch {
                val resource: Resource<Unit> = if (user.value is LoginState.UserLoggedIn) {
                    logOutUserUseCase.logOut()
                } else {
                    loginUserUseCase.login(login, password)
                }
                _login.postValue(resource)
            }
        } catch (e: CancellationException) {
        }
    }
}

sealed class LoginState {
    data class UserLoggedIn(val user: User) : LoginState()
    object UserLoggedOut : LoginState()
}
