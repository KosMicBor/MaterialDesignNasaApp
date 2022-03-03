package kosmicbor.giftapp.pictureofthedayapp.domain.room

import androidx.room.*

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM FavoritesEntity")
    fun all(): List<FavoritesEntity>

    @Query("SELECT * FROM FavoritesEntity WHERE imageUrl LIKE :url")
    fun getFavoritesByUrl(url: String?): FavoritesEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoritesEntity)

    @Update
    fun updateFavorite(favorite: FavoritesEntity)

    @Delete
    fun deleteFavorite(favorite: FavoritesEntity)
}