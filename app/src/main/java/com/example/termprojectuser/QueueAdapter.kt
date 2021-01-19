package com.example.termprojectuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QueueAdapter(val queue:ArrayList<Queue>): RecyclerView.Adapter<QueueAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val queue_txt = itemView.findViewById<TextView>(R.id.queueid_txt)
        fun bind(position: Int){
            queue_txt.text = queue[position].id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.queue_layout_recycle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bind(position)
        }
    }

    override fun getItemCount(): Int {
        return queue.size
    }
}