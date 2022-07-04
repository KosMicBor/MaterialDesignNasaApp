package kosmicbor.giftapp.pictureofthedayapp.domain.moon

import com.google.gson.annotations.SerializedName

data class MoonData(
    @SerializedName("center")
    val center: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("nasa_id")
    val nasaId: String?,

    @SerializedName("media_type")
    val mediaType: String?,

    @SerializedName("keywords")
    val keywords: List<String>,

    @SerializedName("date_created")
    val dateCreated: String?,

    @SerializedName("description_508")
    val description508: String?,

    @SerializedName("secondary_creator")
    val secondaryCreator: String?,

    @SerializedName("description")
    val description: String?,
)