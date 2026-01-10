package com.steinsti.dukawala.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steinsti.dukawala.data.dao.ProductDao
import com.steinsti.dukawala.data.dao.TransactionDao
import com.steinsti.dukawala.data.entity.Product
import com.steinsti.dukawala.data.entity.Transaction

@Database(entities = [Product::class, Transaction::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun transactionDao(): TransactionDao

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