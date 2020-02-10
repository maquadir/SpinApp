package com.maq.spinapp.data

import androidx.room.*

import java.util.*

@Entity(tableName = "item_table")

data class Item(

    @ColumnInfo(name = "item")
    val item: String

){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}






