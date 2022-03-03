package kosmicbor.giftapp.pictureofthedayapp.domain.favorites

import android.os.Handler
import android.os.Looper
import android.util.Log
import kosmicbor.giftapp.pictureofthedayapp.domain.room.FavoritesDao
import kosmicbor.giftapp.pictureofthedayapp.utils.convertEntityToFavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.utils.convertEntityToFavoritesList
import kosmicbor.giftapp.pictureofthedayapp.utils.convertFavoriteItemToEntity

class FavoritesLocalRepoImpl(private val localDataSource: FavoritesDao) : FavoritesLocalRepository {

    private val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

    override fun getAllFavorites(
        getAllFavoritesListener: GetAllFavoritesListener<List<FavoriteItem>>
    ) {

        handler.post {
            Thread {
                try {
                    getAllFavoritesListener.loadSuccess(
                        convertEntityToFavoritesList(
                            localDataSource.all()
                        )
                    )
                } catch (e: Exception) {
                    handler.post { getAllFavoritesListener.loadError(Exception(e.localizedMessage)) }
                }
            }.start()
        }
    }

    override fun saveEntity(item: FavoriteItem) {
        handler.post {
            Thread {
                try {
                    localDataSource.insertFavorite(convertFavoriteItemToEntity(item))
                    Log.d("TAG", convertFavoriteItemToEntity(item).toString())
                    Log.d("TAG", localDataSource.all().toString())
                } catch (e: Exception) {
                    throw e.fillInStackTrace()
                }
            }.start()
        }
    }

    override fun updateEntity(item: FavoriteItem) {

        handler.post {
            Thread {
                localDataSource.updateFavorite(convertFavoriteItemToEntity(item))
            }.start()
        }
    }

    override fun removeEntity(item: FavoriteItem) {
        handler.post {
            Thread {
                localDataSource.deleteFavorite(convertFavoriteItemToEntity(item))
            }.start()
        }
    }

    override fun getEntity(
        url: String?,
        getEntityListener: GetEntityListener<FavoriteItem>
    ) {
        handler.post {
            Thread {
                try {
                    url?.let {
                        Log.d("ENTITY", localDataSource.getFavoritesByUrl(it).toString())
                        val item = convertEntityToFavoriteItem(localDataSource.getFavoritesByUrl(it))

                        getEntityListener.loadSuccess(item)
                    }

                } catch (e: Exception) {
                    getEntityListener.loadError(e.fillInStackTrace())
                }
            }.start()
        }
    }

    interface GetAllFavoritesListener<T> {
        fun loadSuccess(value: T)
        fun loadError(throwable: Throwable)
    }

    interface GetEntityListener<T> {
        fun loadSuccess(value: T)
        fun loadError(throwable: Throwable)
    }
}