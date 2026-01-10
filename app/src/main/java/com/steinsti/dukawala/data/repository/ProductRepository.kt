package com.steinsti.dukawala.data.repository

import com.steinsti.dukawala.data.dao.ProductDao
import com.steinsti.dukawala.data.dao.TransactionDao
import com.steinsti.dukawala.data.entity.Product
import com.steinsti.dukawala.data.entity.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val transactionDao: TransactionDao
) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun getProductById(id: Int): Product? = productDao.getProductById(id)

    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
        // Log initial stock as transaction
        transactionDao.insert(
            Transaction(
                productId = product.id,
                type = "INITIAL_STOCK",
                quantity = product.quantity,
                totalAmount = product.buyingPrice * product.quantity,
                timestamp = System.currentTimeMillis(),
                note = "Onboarding initial stock"
            )
        )
    }

    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
        transactionDao.deleteByProductId(product.id)
    }

    suspend fun recordPurchase(productId: Int, quantityAdded: Int, totalCost: Double) {
        productDao.updateStock(productId, quantityAdded)
        transactionDao.insert(
            Transaction(
                productId = productId,
                type = "PURCHASE",
                quantity = quantityAdded,
                totalAmount = totalCost,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun recordSale(productId: Int, quantitySold: Int, totalRevenue: Double): Boolean {
        val current = productDao.getProductById(productId)?.quantity ?: 0
        if (current < quantitySold) return false

        productDao.updateStock(productId, -quantitySold)
        transactionDao.insert(
            Transaction(
                productId = productId,
                type = "SALE",
                quantity = quantitySold,
                totalAmount = totalRevenue,
                timestamp = System.currentTimeMillis()
            )
        )
        return true
    }

    suspend fun adjustStock(productId: Int, adjustment: Int, reason: String = "Manual adjustment") {
        productDao.updateStock(productId, adjustment)
        transactionDao.insert(
            Transaction(
                productId = productId,
                type = "ADJUSTMENT",
                quantity = adjustment,
                totalAmount = 0.0,
                timestamp = System.currentTimeMillis(),
                note = reason
            )
        )
    }

    // sales calculation logic (starting stock + purchases - current stock)
    suspend fun calculateSalesQuantityForPeriod(
        productId: Int,
        startDateMillis: Long,
        endDateMillis: Long = System.currentTimeMillis()
    ): Int {
        val product = getProductById(productId) ?: return 0
        val currentStock = product.quantity

        val purchasesInPeriod = transactionDao.getPurchasesForProductInPeriod(productId, startDateMillis, endDateMillis)
            .sumOf { it.quantity }

        // Approximate starting stock at period start (current + sales - purchases)
        val salesInPeriod = transactionDao.getSalesForProductInPeriod(productId, startDateMillis, endDateMillis)
            .sumOf { it.quantity }

        val startingStock = currentStock + salesInPeriod - purchasesInPeriod
        return startingStock.coerceAtLeast(0)
    }
}