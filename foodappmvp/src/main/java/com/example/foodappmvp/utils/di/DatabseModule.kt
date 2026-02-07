package com.example.foodappmvp.utils.di

import android.content.Context
import androidx.room.Room
import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.database.FoodDatabase
import com.example.foodappmvp.utils.FOOD_DB_DATABASE
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