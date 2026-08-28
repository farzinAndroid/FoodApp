package com.example.recipeappmvvm.data.model.database

class MyResponseDB<out T>(val status: Status, val data: T? = null, val message: String? = null) {

    enum class Status {  SUCCESS }

    companion object {

        fun <T> success(data: T?): MyResponseDB<T> {
            return MyResponseDB(Status.SUCCESS, data)
        }
    }
}