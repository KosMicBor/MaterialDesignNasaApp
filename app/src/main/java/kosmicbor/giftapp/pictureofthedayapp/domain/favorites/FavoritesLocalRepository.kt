package kosmicbor.giftapp.pictureofthedayapp.domain.favorites

interface FavoritesLocalRepository {
    fun getAllFavorites(
        getAllFavoritesListener: FavoritesLocalRepoImpl.GetAllFavoritesListener<List<FavoriteItem>>
    )

    fun saveEntity(item: FavoriteItem)
    fun updateEntity(item: FavoriteItem)
    fun removeEntity(item: FavoriteItem)
    fun getEntity(
        url: String?,
        getEntityListener: FavoritesLocalRepoImpl.GetEntityListener<FavoriteItem>
    )
}