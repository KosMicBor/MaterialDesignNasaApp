package kosmicbor.giftapp.pictureofthedayapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApodDayData(
    val url: String?,
    val title: String?,
    val description: String?,
    val date: String?
) : Parcelable