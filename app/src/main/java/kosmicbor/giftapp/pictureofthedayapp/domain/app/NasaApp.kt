package kosmicbor.giftapp.pictureofthedayapp.domain.app

import android.app.Application
import androidx.room.Room
import kosmicbor.giftapp.pictureofthedayapp.domain.room.FavoritesDao
import kosmicbor.giftapp.pictureofthedayapp.domain.room.FavoritesDataBase
import java.lang.IllegalStateException

class NasaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: NasaApp? = null
        private var dataBase: FavoritesDataBase? = null
        private const val DATABASE_NAME = "Favorites.db"

        fun getFavoritesDao(): FavoritesDao {

            if (dataBase == null) {
                synchronized(FavoritesDataBase::class.java) {

                    if (dataBase == null) {

                        if (appInstance == null)
                            throw IllegalStateException("Application is null while creating DataBase")

                        dataBase = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            FavoritesDataBase::class.java,
                            DATABASE_NAME
                        )
                            .build()
                    }
                }
            }

            return dataBase!!.favoritesDao()
        }
    }
}
