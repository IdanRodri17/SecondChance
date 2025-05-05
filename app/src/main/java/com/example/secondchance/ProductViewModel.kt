package com.example.secondchance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = ProductRepository(application)
    val productList: LiveData<List<Product>> = repository.getProducts()

    private val _sellerList = MutableLiveData<List<Seller>>()
    val sellerList: LiveData<List<Seller>> = _sellerList

    init {
        loadDummySellers()
    }

    private fun addProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }

    fun addDefaultProducts(products: List<Product>) {
        for (product in products) {
            addProduct(product)
        }
    }
    private fun loadDummySellers() {
        val dummySellers = listOf(
            Seller(
                sellerId = "1",
                name = "for sale product",
                phone = "050-1234567",
                address = "רחוב הדוגמה 10, תל אביב",
                products = listOf(
                    Product(
                        name = "Natan Diaz",
                        description = "אגדה אמיתית",
                        price = "150 ₪",
                        imageRes = R.drawable.nate,
                        imageUri = null,
                        sellerId = "1"
                    ),
                    Product(
                        name = "No des",
                        description = "description",
                        price = "150 ₪",
                        imageRes = R.drawable.nate,
                        imageUri = null,
                        sellerId = "1"
                    )
                )
            ),
            Seller(
                sellerId = "2",
                name = "Second hand for free",
                phone = "050-7654321",
                address = "רחוב ההדגמה 20, ירושלים",
                products = listOf(
                    Product(
                        name = "Diaz Jr",
                        description = "ממשיך דרכו",
                        price = "170 ₪",
                        imageRes = R.drawable.nate,
                        imageUri = null,
                        sellerId = "2"
                    ),
                    Product(
                        name = "Product 2",
                        description = "עוד מוצר שווה",
                        price = "190 ₪",
                        imageRes = R.drawable.nate,
                        imageUri = null,
                        sellerId = "2"
                    )
                )
            )
        )

        _sellerList.value = dummySellers
    }

    fun addProductToSeller(sellerId: String, product: Product) {
        val currentList = _sellerList.value ?: return
        val newList = currentList.map { seller ->
            if (seller.sellerId == sellerId) {
                val updatedProducts = seller.products + product
                seller.copy(products = updatedProducts)
            } else seller
        }
        _sellerList.value = newList

        addProduct(product)
    }
    fun getSellerById(sellerId: String): Seller? {
        return sellerList.value?.find { it.sellerId == sellerId }
    }
//    fun updateProduct(newProduct: Product) {
//        val currentList = _sellerList.value ?: return
//
//        val updatedList = currentList.map { seller ->
//            if (seller.sellerId == newProduct.sellerId) {
//                val updatedProducts = seller.products.map { product ->
//                    if (product.id == newProduct.id) newProduct else product
//                }
//                seller.copy(products = updatedProducts)
//            } else seller
//        }
//
//        _sellerList.value = updatedList
//    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }


    fun updateSellerList(newList: List<Seller>) {
        _sellerList.value = newList
    }

}
