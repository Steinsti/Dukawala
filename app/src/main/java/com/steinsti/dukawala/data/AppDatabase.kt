package com.steinsti.dukawala.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steinsti.dukawala.data.dao.ProductDao
import com.steinsti.dukawala.data.entity.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dukawala_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}