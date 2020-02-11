package com.maq.spinapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maq.spinapp.data.Item

// Room Database Builder
@Database(entities = [Item::class], version = 13, exportSchema = false)
 abstract class ItemDatabase:RoomDatabase() {

    abstract fun getItemDao(): Itemdao

    companion object{

        @Volatile
        private var INSTANCE: ItemDatabase? = null


        fun getDatabase(context: Context): ItemDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "item_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance

                return instance
            }
        }
    }

}