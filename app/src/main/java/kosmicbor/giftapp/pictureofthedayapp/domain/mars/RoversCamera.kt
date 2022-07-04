package kosmicbor.giftapp.pictureofthedayapp.domain.mars

import com.google.gson.annotations.SerializedName

data class RoversCamera(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("rover_id")
    val roverId: Int?,
    @SerializedName("full_name")
    val fullName: String?
)
