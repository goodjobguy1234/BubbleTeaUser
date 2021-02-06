package com.example.termprojectuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class QueueActivity : BaseActivity() {
    lateinit var queueList: FirebaseRecyclerOptions<Queue>
    lateinit var back_btn: ImageButton
    lateinit var queue_recycler: RecyclerView
    lateinit var tvRemain: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        queueList = FirebaseQueueHelper.getOption()
        init()
        queue_recycler.apply {
            layoutManager = LinearLayoutManager(this@QueueActivity)
            adapter = QueueAdapter(queueList){
                tvRemain.text = it.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setUpLayout()
        (queue_recycler.adapter as FirebaseRecyclerAdapter<*, *>).startListening()
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_queue
    }

    fun init(){
        queue_recycler = findViewById(R.id.queue_recycleview)
        back_btn = findViewById(R.id.back_btn)
        tvRemain = findViewById(R.id.tvremain)
    }

    fun onClickBack(view:View){
        finish()
    }

    override fun onStop() {
        super.onStop()
        (queue_recycler.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
    }

}