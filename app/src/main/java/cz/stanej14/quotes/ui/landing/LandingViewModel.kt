package cz.stanej14.quotes.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.landing.ObtainLandingQuoteUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.launch

class LandingViewModel @ViewModelInject constructor(
    private val obtainLandingQuoteUseCase: ObtainLandingQuoteUseCase
) : ViewModel() {

    private val _quote = MutableLiveData<Resource<Quote>>(Resource.Loading)
    val quote: LiveData<Resource<Quote>> = _quote

    init {
        onRefresh()
    }

    fun onRefresh() {
        _quote.value = Resource.Loading
        viewModelScope.launch {
            _quote.postValue(obtainLandingQuoteUseCase.obtainLandingQuote())
        }
    }
}