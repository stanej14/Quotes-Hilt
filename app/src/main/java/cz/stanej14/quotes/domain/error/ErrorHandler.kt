package cz.stanej14.quotes.domain.error

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import cz.stanej14.quotes.domain.util.showSnackbar

class ErrorHandler {
    fun handleError(view: View, error: Throwable) {
        Log.e("TAG", "MSG", error)
        view.showSnackbar("Error")
    }

    fun handleFavoringError(view: View) {
        view.showSnackbar("Favoring Error")
    }
}