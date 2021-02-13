package com.example.termprojectuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val TYPE_ORDER = 1
const val TYPE_REDEEM = 2
const val TYPE_HEADER = 0
class OrderAdapter(val order:ArrayList<RecyclerItem>, val callback: (Int, Int, String) -> Unit):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mcontext: Context
    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.orderimageview)
        val cancel_icon = itemView.findViewById<ImageView>(R.id.cancel_icon)
        val add_icon = itemView.findViewById<ImageView>(R.id.add_imageview)
        val minus_icon = itemView.findViewById<ImageView>(R.id.minus_imageview)
        val ordername = itemView.findViewById<TextView>(R.id.txt_ordername)
        val orderprice = itemView.findViewById<TextView>(R.id.txt_orderprice)
        val quantity = itemView.findViewById<TextView>(R.id.txt_quantity)

        fun bind(position: Int){
            quantity.text = (order[position] as RecyclerItem.Product).order.quantity.toString()
            orderprice.text = (order[position] as RecyclerItem.Product).order.item.price.toString()
            ordername.text = (order[position] as RecyclerItem.Product).order.item.name
            Glide.with(mcontext).load((order[position] as RecyclerItem.Product).order.item.imageUrl).into(image)
        }
    }
    inner class RedeemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.orderimageview)
        val redeemname = itemView.findViewById<TextView>(R.id.txt_ordername)
        val reedeemquantity = itemView.findViewById<TextView>(R.id.txt_orderprice)
        fun bind(position: Int){
            redeemname.text = (order[position] as RecyclerItem.Product).order.item.name
            reedeemquantity.text = (order[position] as RecyclerItem.Product).order.quantity.toString()
            Glide.with(mcontext).load((order[position] as RecyclerItem.Product).order.item.imageUrl).into(image)
        }

    }
    inner class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val header_txt = itemView.findViewById<TextView>(R.id.header_txt)
        fun bind(position: Int){
            header_txt.text = (order[position] as RecyclerItem.Header).typeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        val holder: RecyclerView.ViewHolder? =
        when(viewType){
            TYPE_ORDER -> {
                val view = LayoutInflater.from(mcontext).inflate(R.layout.orderlayout, parent, false)
                 OrderViewHolder(view)
            }
            TYPE_REDEEM -> {
                val view = LayoutInflater.from(mcontext).inflate(R.layout.reward_order_layout, parent, false)
                RedeemViewHolder(view)
            }
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.header_layout, parent, false)
                HeaderHolder(view)
            }

            else -> null
        }
        return holder!!
    }

    override fun getItemCount(): Int {
        return order.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TYPE_ORDER -> {
                (holder as OrderViewHolder).apply {
                    bind(position)
                    cancel_icon.setOnClickListener {
                        callback(position, 1, (order[position] as RecyclerItem.Product).order.item.name)
                    }
                    minus_icon.setOnClickListener {
                        callback(position, 3, (order[position] as RecyclerItem.Product).order.item.name)
                    }
                    add_icon.setOnClickListener {
//                        if ((order[position] as RecyclerItem.Product).order.item.checkRemain()){
//                            callback(position, 2, (order[position] as RecyclerItem.Product).order.item.name)
//                        }else{
//                            Toast.makeText(mcontext, "This Menu Sold Out", Toast.LENGTH_LONG).show()
//                        }
                        callback(position, 2, (order[position] as RecyclerItem.Product).order.item.name)

                    }
                }
            }
            TYPE_REDEEM -> {
                (holder as RedeemViewHolder).apply {
                    bind(position)
                }
            }
            TYPE_HEADER -> {
                (holder as HeaderHolder).bind(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(order[position]){
            is RecyclerItem.Header -> TYPE_HEADER
            is RecyclerItem.Product -> {
                if ((order[position] as RecyclerItem.Product).order.reward){
                    TYPE_REDEEM
                }else{
                    TYPE_ORDER
                }
            }
        }
    }

}