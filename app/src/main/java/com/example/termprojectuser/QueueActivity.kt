package com.example.termprojectuser

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.termprojectuser.Adapter.QueueAdapter
import com.example.termprojectuser.Entity.Queue
import com.example.termprojectuser.FirebaseHelper.FirebaseQueueHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class QueueActivity : BaseActivity() {
    private lateinit var queueList: FirebaseRecyclerOptions<Queue>
    private lateinit var back_btn: ImageButton
    private lateinit var queue_recycler: RecyclerView
    private lateinit var tvRemain: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        queueList = FirebaseQueueHelper.getOption()
        init()

        //  display queue list
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

//    set up ui
    override fun getLayoutResourceId() = R.layout.activity_queue

    //  map variable with ui
    private fun init(){
        queue_recycler = findViewById(R.id.queue_recycleview)
        back_btn = findViewById(R.id.back_btn)
        tvRemain = findViewById(R.id.tvremain)
    }

    //  when user click back at the top left
    fun onClickBack(view:View){
        finish()
    }

    override fun onStop() {
        super.onStop()
        (queue_recycler.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
    }

}