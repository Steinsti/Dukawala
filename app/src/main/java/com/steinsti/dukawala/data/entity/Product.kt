package com.steinsti.dukawala.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,  // e.g., "Beverages"
    val quantity: Int,
    val buyingPrice: Double,
    val sellingPrice: Double,
    val barcode: String? = null
)