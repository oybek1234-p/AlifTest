package com.uz.aliftest.data

import androidx.room.*
@Dao
interface MessageDao {
    @Query("SELECT * FROM post")
    fun getAllList(): List<Post>

    @Delete
    fun delete(post: Post)

    @Insert
    fun insertAll(list: List<Post>)
}

@Database(entities = [Post::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}