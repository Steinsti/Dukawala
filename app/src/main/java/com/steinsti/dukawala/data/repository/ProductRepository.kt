package com.steinsti.dukawala.data.repository

import com.steinsti.dukawala.data.dao.ProductDao
import com.steinsti.dukawala.data.entity.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject  // We'll add DI later; for now it's optional

class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    suspend fun update(product: Product) {
        productDao.update(product)
    }

    suspend fun delete(product: Product) {
        productDao.delete(product)
    }

    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
}