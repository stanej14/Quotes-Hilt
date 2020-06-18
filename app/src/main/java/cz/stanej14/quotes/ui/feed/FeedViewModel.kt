package cz.stanej14.quotes.ui.feed

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.stanej14.quotes.domain.feed.ObtainQuotesUseCase
import cz.stanej14.quotes.model.Quote
import cz.stanej14.quotes.model.Resource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedViewModel @ViewModelInject constructor(
    private val obtainQuotesUseCase: ObtainQuotesUseCase
) : ViewModel() {

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }

    private val _quotes = MutableLiveData<Resource<List<Quote>>>(Resource.Loading)
    val quotes: LiveData<Resource<List<Quote>>> = _quotes

    private var queryJob: Job? = null
    private var debounceJob: Job? = null
    private var previousQuery: String? = null

    init {
        loadData()
    }

    fun onRefresh() {
        loadData()
    }

    fun onTagClicked(tag: String) {
        debounceJob?.cancel()
        loadData(tag = tag)
    }

    private var debounceHelper: String? = ""
    fun onQueryUpdated(newText: String?) {
        val query = newText.takeIf { it?.isNotBlank() ?: false }
        debounceHelper = query

        // Simple debounce to prevent multiple calls in a short span.
        try {
            debounceJob = viewModelScope.launch {
                delay(DEBOUNCE_MILLIS)
                if (debounceHelper != query || previousQuery == query)
                    return@launch

                previousQuery = query
                loadData(query)
            }
        } catch (e: CancellationException) {
            // Ignoring.
        }
    }

    private fun loadData(query: String? = null, tag: String? = null) {
        _quotes.value = Resource.Loading
        queryJob?.cancel()
        try {
            queryJob = viewModelScope.launch {
                val data = when {
                    tag != null -> obtainQuotesUseCase.obtainQuotes(tag, true)
                    else -> obtainQuotesUseCase.obtainQuotes(query)
                }
                _quotes.postValue(data)
            }
        } catch (e: CancellationException) {
            // Ignoring.
        }
    }
}