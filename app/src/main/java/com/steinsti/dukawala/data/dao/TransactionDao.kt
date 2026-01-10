package com.steinsti.dukawala.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.steinsti.dukawala.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE productId = :productId ORDER BY timestamp DESC")
    fun getTransactionsForProduct(productId: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE productId = :productId AND type = 'PURCHASE' AND timestamp BETWEEN :start AND :end")
    suspend fun getPurchasesForProductInPeriod(productId: Int, start: Long, end: Long): List<Transaction>

    @Query("SELECT * FROM transactions WHERE productId = :productId AND type = 'SALE' AND timestamp BETWEEN :start AND :end")
    suspend fun getSalesForProductInPeriod(productId: Int, start: Long, end: Long): List<Transaction>

    @Query("DELETE FROM transactions WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)
}