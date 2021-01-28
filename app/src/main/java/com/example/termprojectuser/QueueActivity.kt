package com.example.termprojectuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class QueueActivity : AppCompatActivity() {
    lateinit var queueList:ArrayList<Queue>
    lateinit var back_btn: ImageButton
    lateinit var queue_recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        queueList = Queue.createList()
        init()
        queue_recycler.apply {
            layoutManager = LinearLayoutManager(this@QueueActivity)
            adapter = QueueAdapter(queueList)
        }
    }

    override fun onStart() {
        super.onStart()
        setUpLayout()
    }
    fun setUpLayout(){
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setUpLayout()
    }

    fun init(){
        queue_recycler = findViewById(R.id.queue_recycleview)
        back_btn = findViewById(R.id.back_btn)
    }

    fun onClickBack(view:View){
        finish()
    }
}