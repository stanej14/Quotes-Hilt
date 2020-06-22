package cz.stanej14.quotes.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.stanej14.quotes.R
import cz.stanej14.quotes.domain.error.ErrorHandler
import cz.stanej14.quotes.domain.util.showSnackbar
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var errorHandler: ErrorHandler

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_login_cta.setOnClickListener {
            viewModel.onCtaClicked(edit_login.text.toString(), edit_password.text.toString())
        }
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        with(viewModel) {
            login.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Loading -> showLoading()
                    is Resource.Error -> showError(it.error)
                    is Resource.Success -> showSuccess()
                }
            })
            user.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is LoginState.UserLoggedOut -> renderLoggedOutState()
                    is LoginState.UserLoggedIn -> renderLoggedInState(it.user)
                }
            })
        }
    }

    private fun renderLoggedOutState() {
        btn_login_cta.setText(R.string.action_login)
        renderViews(true)
    }

    private fun renderLoggedInState(user: User) {
        text_email.text = user.email
        text_login.text = user.login
        btn_login_cta.setText(R.string.action_logout)
        renderViews(false)
    }

    private fun renderViews(shouldShowLogin: Boolean) {
        input_login.isVisible = shouldShowLogin
        input_password.isVisible = shouldShowLogin
        text_email.isVisible = !shouldShowLogin
        text_login.isVisible = !shouldShowLogin
    }

    private fun showSuccess() {
        btn_login_cta.isVisible = true
        progress_login.isVisible = false
        requireActivity().showSnackbar(R.string.login_success)
    }

    private fun showError(error: Throwable) {
        progress_login.isVisible = false
        btn_login_cta.isVisible = true
        errorHandler.handleError(requireView(), error)
    }

    private fun showLoading() {
        progress_login.isVisible = true
        btn_login_cta.isVisible = false
    }
}