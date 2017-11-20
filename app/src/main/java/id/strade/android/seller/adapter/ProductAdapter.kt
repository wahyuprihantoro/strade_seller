package id.strade.android.seller.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import id.strade.android.seller.R
import id.strade.android.seller.model.Product

/**
 * Created by wahyu on 18 November 2017.
 */
class ProductAdapter(var context: Context, var products: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun getItemCount() = products.size


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageView)
        var name: TextView = itemView.findViewById(R.id.name)
        var price: TextView = itemView.findViewById(R.id.price)
        fun bind(product: Product) {
            Glide.with(context).load(product.imageUrl).into(image)
            name.text = product.name
            price.text = product.price.toString()
        }
    }
}