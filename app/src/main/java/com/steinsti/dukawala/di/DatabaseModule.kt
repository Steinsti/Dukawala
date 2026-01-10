package com.steinsti.dukawala.di

import android.content.Context
import androidx.room.Room
import com.steinsti.dukawala.data.AppDatabase
import com.steinsti.dukawala.data.dao.ProductDao
import com.steinsti.dukawala.data.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dukawala_database"
        )
        .fallbackToDestructiveMigration(dropAllTables = true) // Handle version changes during development
        .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }
}
