package cz.stanej14.quotes.domain.error

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar

class ErrorHandler {
    fun handleError(view: View, error: Throwable) {
        Log.e("TAG", "MSG", error)
        Snackbar.make(view, "Error", Snackbar.LENGTH_SHORT).show()
    }
}