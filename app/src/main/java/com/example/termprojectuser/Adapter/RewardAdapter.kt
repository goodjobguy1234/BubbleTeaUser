package com.example.termprojectuser.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.termprojectuser.Entity.Menu
import com.example.termprojectuser.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class RewardAdapter(options: FirebaseRecyclerOptions<Menu>, val callback: (Menu) -> Unit): FirebaseRecyclerAdapter<Menu, RewardAdapter.ViewHolder>(options) {
    private lateinit var mcontext:Context
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imageView)
        val rewardName:TextView = itemView.findViewById(R.id.txt_name)
        val layout:ConstraintLayout = itemView.findViewById(R.id.item_layout)
        val rewardPrice:TextView = itemView.findViewById(R.id.txt_price)
        fun bind(model: Menu){
            rewardName.text = model.name
            rewardPrice.text = model.point.toString()
            Glide.with(mcontext).load(model.imageUrl).into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mcontext = parent.context
        val view = LayoutInflater.from(mcontext).inflate(R.layout.reward_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Menu) {
        holder.apply {
            bind(model)
            layout.setOnClickListener {
                callback(model)
            }
        }
    }
}