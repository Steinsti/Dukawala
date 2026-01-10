package com.steinsti.dukawala.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,        // Foreign key to Product
    val type: String,          // "PURCHASE", "SALE", "ADJUSTMENT", "INITIAL_STOCK"
    val quantity: Int,
    val totalAmount: Double,   // quantity * price at time
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null   // For storing reasons for adjustments or initial stock info
)
