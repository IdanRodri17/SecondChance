package com.example.secondchance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SellerAdapter(
    private val sellers: List<Seller>,
    private val onProductClick: (Product) -> Unit,
    private val onProductLongClick: (Product) -> Unit
) : RecyclerView.Adapter<SellerAdapter.SellerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seller, parent, false)
        return SellerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SellerViewHolder, position: Int) {
        holder.bind(sellers[position])
    }

    override fun getItemCount() = sellers.size

    inner class SellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sellerName: TextView = itemView.findViewById(R.id.tvSellerName)
        private val rvProducts: RecyclerView = itemView.findViewById(R.id.rvSellerProducts)

        fun bind(seller: Seller) {
            sellerName.text = seller.name

            rvProducts.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            val productAdapter = ProductAdapter(
                onItemClick = { product ->
                    onProductClick(product)
                },
                onItemLongClick = { product ->
                    onProductLongClick(product)
                }
            )

            rvProducts.adapter = productAdapter
            productAdapter.submitList(seller.products)
        }
    }
}
