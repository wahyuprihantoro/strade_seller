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
import id.strade.android.seller.model.Order

/**
 * Created by wahyu on 25 November 2017.
 */
class OrderAdapter(var context: Context, var orders: List<Order>) :
        RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    override fun onBindViewHolder(holder: OrderViewHolder?, position: Int) {
        holder?.bind(orders[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = itemView.findViewById(R.id.imageView)
        var name: TextView = itemView.findViewById(R.id.name)
        var price: TextView = itemView.findViewById(R.id.price)
        var distance: TextView = itemView.findViewById(R.id.distance)
        fun bind(order: Order) {
            Glide.with(context).load(order.buyer.imageUrl).into(image)
            name.text = order.buyer.fullName
            price.text = "Total harga: ${order.totalPrice}"
            distance.text = "${order.distance} KM"
        }
    }
}