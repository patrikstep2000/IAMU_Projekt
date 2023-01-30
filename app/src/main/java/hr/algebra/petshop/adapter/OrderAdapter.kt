package hr.algebra.petshop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.petshop.R
import hr.algebra.petshop.model.Order

class OrderAdapter(private val context: Context, private val orders: MutableList<Order>)
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPetId: TextView = itemView.findViewById(R.id.tvPetId)
        private val tvArrived: TextView = itemView.findViewById(R.id.tvArrived)

        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            tvPetId.text = "Pet id: ${order.pet_id}"
            tvArrived.text = "Arrived: ${order.arrived}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(orders[position]) }

    override fun getItemCount(): Int = orders.size

}