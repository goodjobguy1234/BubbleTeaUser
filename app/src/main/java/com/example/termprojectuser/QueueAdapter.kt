package com.example.termprojectuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class QueueAdapter(options: FirebaseRecyclerOptions<Queue>, val callback: (Int) -> Unit): FirebaseRecyclerAdapter<Queue, QueueAdapter.ViewHolder>(options) {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val queue_txt = itemView.findViewById<TextView>(R.id.queueid_txt)
        fun bind(model: Queue){
            queue_txt.text = model.queueId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.queue_layout_recycle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Queue) {
        holder.apply {
            bind(model)
            callback(itemCount)
        }
    }
}