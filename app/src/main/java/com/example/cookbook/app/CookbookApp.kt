package com.example.cookbook.app

import android.app.Application
import androidx.room.Room
import com.example.cookbook.di.appModule
import com.example.cookbook.di.favoritesFragment
import com.example.cookbook.di.homeFragment
import com.example.cookbook.di.localDataBase
import com.example.cookbook.di.network
import com.example.cookbook.di.randomRecipeFragment
import com.example.cookbook.di.recipeInfo
import com.example.cookbook.di.remoteDataSource
import com.example.cookbook.di.searchFragment
import com.example.cookbook.model.room.IRecipesDAO
import com.example.cookbook.model.room.RecipesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CookbookApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CookbookApp)
            modules(
                listOf(
                    appModule,
                    remoteDataSource,
                    localDataBase,
                    network,
                    homeFragment,
                    searchFragment,
                    randomRecipeFragment,
                    recipeInfo,
                    favoritesFragment
                )
            )
        }
    }

    companion object {
        private var appInstance: CookbookApp? = null
        private var db: RecipesDatabase? = null
        private const val DB_NAME = "recipesdatabase.db"
        fun getRecipesDAO(): IRecipesDAO {
            if (db == null) {
                if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                db = Room.databaseBuilder(
                    appInstance!!.applicationContext,
                    RecipesDatabase::class.java,
                    DB_NAME
                ).build()
            }
            return db!!.getRecipesDAO()
        }
    }
}