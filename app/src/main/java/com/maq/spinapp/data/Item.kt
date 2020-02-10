package com.maq.spinapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Item data class
@Entity(tableName = "item_table")
data class Item(

    @ColumnInfo(name = "item")
    val item: String

){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}






