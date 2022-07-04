package kosmicbor.giftapp.pictureofthedayapp.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoritesEntity(

    @PrimaryKey
    val imageUrl: String,
    val date: String?,
    val title: String?,
    val description: String?
)
