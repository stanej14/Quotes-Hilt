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
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quote

        if (id != other.id) return false
        if (tags != other.tags) return false
        if (body != other.body) return false
        if (author != other.author) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }
}