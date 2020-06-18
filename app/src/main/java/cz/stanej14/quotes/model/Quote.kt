package cz.stanej14.quotes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Quote(
    val id: Long,
    val tags: List<String>,
    val body: String,
    val author: String,
    val isFavorite: Boolean
) : Parcelable