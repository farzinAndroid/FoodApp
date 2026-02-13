package com.example.recipeappmvvm.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeappmvvm.data.model.database.FoodEntity
import com.example.recipeappmvvm.utils.FOOD_DB_TABLE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFood(food: FoodEntity)


    @Delete
    suspend fun deleteFood(food: FoodEntity)


    @Query("SELECT * FROM $FOOD_DB_TABLE")
    fun getAllFoods() : Flow<MutableList<FoodEntity>>

    @Query("select exists (select 1 from $FOOD_DB_TABLE where id = :id)")
    fun isFoodExist(id: Int) : Flow<Boolean>
}