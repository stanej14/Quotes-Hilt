package cz.stanej14.quotes.domain.util

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import cz.stanej14.quotes.R

fun Activity.showSnackbar(message: String) {
    val rootView: View = findViewById<View>(R.id.container) // Coordinator layout
        ?: findViewById(android.R.id.content)
        ?: return
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.showSnackbar(@StringRes messageRes: Int) =
    showSnackbar(getString(messageRes))

fun View.showSnackbar(message: String) {
    (context as? Activity)?.showSnackbar(message)
}
