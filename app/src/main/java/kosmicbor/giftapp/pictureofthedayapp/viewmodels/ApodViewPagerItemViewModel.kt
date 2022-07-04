package kosmicbor.giftapp.pictureofthedayapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.domain.app.NasaApp
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoritesLocalRepoImpl
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState

class ApodViewPagerItemViewModel(
    private val liveDataToObserve: MutableLiveData<Boolean> = MutableLiveData(),
    private val localRepository: FavoritesLocalRepoImpl = FavoritesLocalRepoImpl(NasaApp.getFavoritesDao())
) : ViewModel() {

    fun getData(): LiveData<Boolean> = liveDataToObserve

    fun addToFavorites(item: FavoriteItem) {
        localRepository.saveEntity(item)
    }

    fun removeFromFavorites(url: String?) {
        localRepository.getEntity(
            url,
            object : FavoritesLocalRepoImpl.GetEntityListener<FavoriteItem> {

                override fun loadSuccess(value: FavoriteItem) {
                    Log.d("URL", value.toString())
                    localRepository.removeEntity(value)

                }

                override fun loadError(throwable: Throwable) {
                    throw Exception(throwable.localizedMessage)
                }
            })
    }

    fun isItemFavorite(url: String?) {

        localRepository.getAllFavorites(object :
            FavoritesLocalRepoImpl.GetAllFavoritesListener<List<FavoriteItem>> {
            override fun loadSuccess(value: List<FavoriteItem>) {
                if (!value.isNullOrEmpty()) {
                    value.forEach {
                        if (it.imageUrl == url) {
                            liveDataToObserve.postValue(true)
                        }
                    }
                } else {
                    liveDataToObserve.postValue(false)
                }

            }

            override fun loadError(throwable: Throwable) {
                throw Exception(throwable.localizedMessage)
            }
        })
    }
}
