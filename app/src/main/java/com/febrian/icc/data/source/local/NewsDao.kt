package com.febrian.icc.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert
    fun insert(news : EntityNews)

    @Query("DELETE from news where title = :title")
    fun delete(title : String)

    @Query("SELECT * from news order by id asc")
    fun getAllNews():List<EntityNews>

    @Query("SELECT * from news where id = :id ")
    fun getNewsById(id : Int) : EntityNews

    @Query("SELECT * from news where title = :title")
    fun getNewsByTitle(title : String) : EntityNews

    @Query("SELECT EXISTS(SELECT * from news where title = :title)")
    fun newsExist(title: String) : Boolean
}