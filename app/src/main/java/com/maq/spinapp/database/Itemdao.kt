package com.maq.spinapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maq.spinapp.data.Item


@Dao
interface Itemdao {

    @Query("SELECT * FROM item_table")
    fun getAll(): List<Item>

    @Insert
    fun insert(item:Item)

    @Query("SELECT COUNT(item) FROM item_table")
    fun getRowCount(): Int


}