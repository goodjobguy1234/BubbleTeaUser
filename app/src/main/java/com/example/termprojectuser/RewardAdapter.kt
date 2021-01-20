package com.example.termprojectuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

class RewardAdapter(val reward:ArrayList<RewardMenu>, val callback: (RewardMenu, Int) -> Unit): RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imageView:ImageView = itemView.findViewById(R.id.imageView)
        val rewardName:TextView = itemView.findViewById(R.id.txt_name)
        val layout:ConstraintLayout = itemView.findViewById(R.id.item_layout)
        val rewardPrice:TextView = itemView.findViewById(R.id.txt_price)
        fun bind(position: Int){
            rewardName.text = reward[position].menu.name
            rewardPrice.text = reward[position].point.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reward_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(position)
            layout.setOnClickListener {
                callback(reward[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
       return reward.size
    }
}