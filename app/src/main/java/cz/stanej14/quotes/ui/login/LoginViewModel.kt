package cz.stanej14.quotes.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.session.LoginUserUseCase
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _login = MutableLiveData<Resource<Unit>>()
    val login: LiveData<Resource<Unit>> = _login

    fun onLoginClicked(login: String, password: String) {
        _login.value = Resource.Loading
        try {
            viewModelScope.launch {
                _login.postValue(loginUserUseCase.login(login, password))
            }
        } catch (e: CancellationException) {
        }
    }
}