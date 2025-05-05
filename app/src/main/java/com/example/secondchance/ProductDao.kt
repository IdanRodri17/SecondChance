package com.example.secondchance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Insert
    fun insertProduct(products: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    fun deleteProduct(vararg product: Product)

    @Query("SELECT * FROM ProductsTable ORDER BY Name ASC")
    fun getProduct(): LiveData<List<Product>>

    @Query("SELECT * FROM ProductsTable ORDER BY Price ASC")
    fun getProductsByPriceAsc(): LiveData<List<Product>>

    @Query("SELECT * FROM ProductsTable ORDER BY Price DESC")
    fun getProductsByPriceDesc(): LiveData<List<Product>>

    @Query("SELECT * FROM ProductsTable WHERE id = :id")
    fun getProductById(id: Int): LiveData<Product?>
}
