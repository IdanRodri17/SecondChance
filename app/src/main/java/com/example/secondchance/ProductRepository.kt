package com.example.secondchance

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductRepository(application: Application) {

    private val productsDao: ProductDao

    init {
        val db = AppDatabase.getDatabase(application)
        productsDao = db.ProductsDau()
    }

    fun getProducts(): LiveData<List<Product>> = productsDao.getProduct()

    fun addProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productsDao.addProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productsDao.deleteProduct(product)
        }
    }

    fun getProductsByName(): LiveData<List<Product>> = productsDao.getProduct()

    fun getProductsByPriceAsc(): LiveData<List<Product>> = productsDao.getProductsByPriceAsc()

    fun getProductsByPriceDesc(): LiveData<List<Product>> = productsDao.getProductsByPriceDesc()

    fun getProduct(id: Int): LiveData<Product?> = productsDao.getProductById(id)

    fun updateProduct(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productsDao.updateProduct(product)
        }
    }
}
