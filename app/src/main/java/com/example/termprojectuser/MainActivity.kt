package com.example.termprojectuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var menu:ArrayList<Menu>
    private lateinit var menu_recycleView:RecyclerView
    private lateinit var confirm_btn:Button
    private lateinit var queue_btn:Button
    private lateinit var redeem_btn:Button
    private lateinit var order_recycleView:RecyclerView
    private lateinit var order:ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        createMenu()
        order = ArrayList()
        createOrder()
        val menuLayout = GridLayoutManager(this,2)
        val orderLayout = LinearLayoutManager(this)
        menu_recycleView.apply {
            layoutManager = menuLayout
            adapter = MenuAdapter(menu){item ->
                var position = order.size
                if(order.any { it.item.name == item.name }){
                    order.forEach {
                        if (it.item.name == item.name){
                            it.quantity++
                            position = order.indexOf(it)
                        }
                    }
                }
                else{
                    order.add(Order(item,1))
                }
                order_recycleView.adapter?.notifyDataSetChanged()
                order_recycleView.scrollToPosition(position)
            }
        }

        order_recycleView.apply {
            layoutManager = orderLayout
            adapter = OrderAdapter(order){position,type ->
                when(type){
                    1 -> order.removeAt(position)
                    2 -> order[position].quantity ++
                    3 -> {
                        order[position].quantity--
                        if (order[position].quantity == 0){
                            order.removeAt(position)
                        }
                    }

                }
                order_recycleView.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun createMenu(){
        menu = ArrayList()
        menu.add(Menu(1234, "item1", 35))
        menu.add(Menu(1234, "item2", 35))
        menu.add(Menu(1234, "item3", 35))
        menu.add(Menu(1234, "item4", 35))
        menu.add(Menu(1234, "item5", 35))

    }
    fun init(){
        menu_recycleView = findViewById<RecyclerView>(R.id.menu_recycleview)
        order_recycleView = findViewById<RecyclerView>(R.id.order_recycleview)
        confirm_btn = findViewById(R.id.confirm_btn)
        queue_btn = findViewById(R.id.queue_btn)
        redeem_btn = findViewById(R.id.redeem_btn)
    }
    fun createOrder(){

        var item = Menu(1234, "item1", 35)
        order.add(Order(item, 1))
        item = Menu(1234, "item2", 35)
        order.add(Order(item, 1))
        item = Menu(1234, "item3", 35)
        order.add(Order(item, 1))
        item = Menu(1234, "item4", 35)
        order.add(Order(item, 1))
    }
    override fun onStart() {
        super.onStart()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    fun onQueueBtnClickListerner(view: View){
        val intent = Intent(this, QueueActivity::class.java)
        startActivity(intent)
    }

}