package com.steinsti.dukawala.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steinsti.dukawala.data.entity.Product
import com.steinsti.dukawala.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel  // Optional for now; we'll add Hilt later
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // Remove if not using Hilt yet
class InventoryViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addProduct(name: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            val product = Product(
                name = name.trim(),
                quantity = quantity.coerceAtLeast(0),
                price = price.coerceAtLeast(0.0)
            )
            repository.insert(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }
}