package kosmicbor.giftapp.pictureofthedayapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kosmicbor.giftapp.pictureofthedayapp.domain.app.NasaApp
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoriteItem
import kosmicbor.giftapp.pictureofthedayapp.domain.favorites.FavoritesLocalRepoImpl
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState.Success
import kosmicbor.giftapp.pictureofthedayapp.utils.AppState.Error
import kosmicbor.giftapp.pictureofthedayapp.utils.convertFavoriteItemToEntity

class FavoritesViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: FavoritesLocalRepoImpl = FavoritesLocalRepoImpl(NasaApp.getFavoritesDao())
) : ViewModel() {

    private var localFavoritesList = mutableListOf<FavoriteItem>()

    fun getdata(): LiveData<AppState> = liveDataToObserve

    fun getFavoritesList() {
        repository.getAllFavorites(object :
            FavoritesLocalRepoImpl.GetAllFavoritesListener<List<FavoriteItem>> {
            override fun loadSuccess(value: List<FavoriteItem>) {
                liveDataToObserve.postValue(Success(value))
            }

            override fun loadError(throwable: Throwable) {
                liveDataToObserve.postValue(Error(throwable))
            }
        })
    }

    fun removeFromFavorites(item: FavoriteItem) {
        repository.removeEntity(item)
    }
}