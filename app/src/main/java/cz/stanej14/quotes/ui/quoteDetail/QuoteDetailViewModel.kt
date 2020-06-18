package cz.stanej14.quotes.ui.quoteDetail

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.favorite.ToggleFavoriteQuoteUseCase
import cz.stanej14.quotes.domain.livedata.Event
import cz.stanej14.quotes.domain.session.HasUserSessionUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class QuoteDetailViewModel @ViewModelInject constructor(
    private val toggleFavoriteQuoteUseCase: ToggleFavoriteQuoteUseCase,
    private val hasUserSessionUseCase: HasUserSessionUseCase
) : ViewModel() {

    private val _quote = MutableLiveData<Resource<Quote>>()
    val quote: LiveData<Resource<Quote>> = _quote
    private var lastQuote: Quote? = null

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    fun onFavoriteClicked() {
        // User has to be logged in.
        if (!hasUserSessionUseCase.hasUserSession()) {
            _loginEvent.value = Event(Unit)
            return
        }

        val quote = lastQuote ?: kotlin.run {
            Log.e("TAG", "Should not happen")
            return
        }
        _quote.value = Resource.Loading
        try {
            viewModelScope.launch {
                val toggleFavorite = toggleFavoriteQuoteUseCase.toggleFavorite(quote)
                if (toggleFavorite is Resource.Success) lastQuote = toggleFavorite.data
                _quote.postValue(toggleFavorite)
            }
        } catch (e: CancellationException) {
            //
        }
    }

    fun initialize(quote: Quote) {
        lastQuote = quote
        _quote.value = Resource.Success(quote)
    }
}