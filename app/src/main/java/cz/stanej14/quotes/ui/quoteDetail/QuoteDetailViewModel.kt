package cz.stanej14.quotes.ui.quoteDetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.detail.ObserveQuoteUseCase
import cz.stanej14.quotes.domain.detail.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.domain.livedata.Event
import cz.stanej14.quotes.domain.session.HasUserSessionUseCase
import cz.stanej14.quotes.domain.util.cancelIfActive
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import cz.stanej14.quotes.model.data
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class QuoteDetailViewModel @ViewModelInject constructor(
    private val toggleFavoriteQuoteUseCase: ToggleFavoriteQuoteUseCase,
    private val observeQuoteUseCase: ObserveQuoteUseCase,
    private val hasUserSessionUseCase: HasUserSessionUseCase
) : ViewModel() {

    private val _quote = MutableLiveData<Resource<Quote>>()
    val quote: LiveData<Resource<Quote>> = _quote

    private var quoteId: Long? = null
    private var observeQuoteJob: Job? = null

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _favorite = MutableLiveData<Resource<Unit>>()
    val favorite: LiveData<Resource<Unit>> = _favorite

    fun onFavoriteClicked() {
        // User has to be logged in.
        if (!hasUserSessionUseCase.hasUserSession()) {
            _loginEvent.value = Event(Unit)
            return
        }

        check(quoteId != null)

        _favorite.value = Resource.Loading
        try {
            viewModelScope.launch {
                val quote = _quote.value?.data
                _favorite.postValue(toggleFavoriteQuoteUseCase.toggleFavorite(quote!!))
            }
        } catch (e: Exception) {
            _favorite.postValue(Resource.Error(e))
        }
    }

    fun initialize(quote: Quote) {
        quoteId = quote.id
        observeQuoteJob?.cancelIfActive()
        viewModelScope.launch {
            observeQuoteUseCase.observeQuote(quote.id).collect { resource ->
                _quote.postValue(resource)
            }
        }
    }
}