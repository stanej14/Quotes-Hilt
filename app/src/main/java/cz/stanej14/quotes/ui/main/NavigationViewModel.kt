package cz.stanej14.quotes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.stanej14.quotes.domain.livedata.Event
import cz.stanej14.quotes.model.Quote

class NavigationViewModel : ViewModel() {

    private val _navigationEvents = MutableLiveData<Event<NavigationEvent>>()
    val navigationEvents: LiveData<Event<NavigationEvent>> = _navigationEvents

    fun navigateInside() {
        _navigationEvents.value = Event(NavigationEvent.Feed)
    }

    fun navigateToLogin() {
        _navigationEvents.value = Event(NavigationEvent.Login)
    }

    fun navigateToQuoteDetail(quote: Quote) {
        _navigationEvents.value = Event(NavigationEvent.QuoteDetail(quote))
    }

    sealed class NavigationEvent {
        object Feed : NavigationEvent()
        object Login : NavigationEvent()
        data class QuoteDetail(val quote: Quote) : NavigationEvent()
    }
}