package com.example.foodappmvp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.utils.FOOD_DB_TABLE
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFood(food: FoodEntity) : Completable


    @Delete
    fun deleteFood(food: FoodEntity) : Completable


    @Query("SELECT * FROM $FOOD_DB_TABLE")
    fun getAllFoods() : Observable<MutableList<FoodEntity>>

    @Query("select exists (select 1 from $FOOD_DB_TABLE where id = :id)")
    fun isFoodExist(id: Int) : Observable<Boolean>
}