package com.steinsti.dukawala.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steinsti.dukawala.data.entity.Product
import com.steinsti.dukawala.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    // Reactive list of all products (current stock)
    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Add new product (used during onboarding or adding new items)
    fun addProduct(name: String, category: String, quantity: Int, buyingPrice: Double, sellingPrice: Double) {
        viewModelScope.launch {
            val product = Product(
                name = name.trim(),
                category = category,
                quantity = quantity.coerceAtLeast(0),
                buyingPrice = buyingPrice.coerceAtLeast(0.0),
                sellingPrice = sellingPrice.coerceAtLeast(0.0)
            )
            repository.insertProduct(product)
        }
    }

    // Record a purchase (increases stock)
    fun recordPurchase(productId: Int, quantityAdded: Int, totalCost: Double) {
        viewModelScope.launch {
            repository.recordPurchase(productId, quantityAdded, totalCost)
        }
    }

    // Record a sale (decreases stock, checks for sufficient quantity)
    fun recordSale(productId: Int, quantitySold: Int, totalRevenue: Double) {
        viewModelScope.launch {
            val success = repository.recordSale(productId, quantitySold, totalRevenue)
            if (!success) {
                // TODO: Show error message in UI (e.g., toast or snackbar)
                println("Sale failed: Insufficient stock for product $productId")
            }
        }
    }

    // Manual stock adjustment (e.g., during stock take)
    fun adjustStock(productId: Int, adjustment: Int, reason: String = "Manual adjustment") {
        viewModelScope.launch {
            repository.adjustStock(productId, adjustment, reason)
        }
    }

    // Example: Get sales quantity for a period (your formula)
    suspend fun getSalesForPeriod(productId: Int, startDateMillis: Long): Int {
        return repository.calculateSalesQuantityForPeriod(productId, startDateMillis)
    }

    // Optional: Delete product
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}