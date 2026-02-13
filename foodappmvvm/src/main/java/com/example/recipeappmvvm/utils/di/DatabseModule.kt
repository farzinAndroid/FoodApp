package com.example.recipeappmvvm.utils.di

import android.content.Context
import androidx.room.Room
import com.example.recipeappmvvm.data.database.FoodDatabase
import com.example.recipeappmvvm.data.model.database.FoodEntity
import com.example.recipeappmvvm.utils.FOOD_DB_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        FoodDatabase::class.java,
        FOOD_DB_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideFoodDao(database: FoodDatabase) = database.getFoodDao()


    @Provides
    @Singleton
    fun provideFoodEntity() = FoodEntity()

}