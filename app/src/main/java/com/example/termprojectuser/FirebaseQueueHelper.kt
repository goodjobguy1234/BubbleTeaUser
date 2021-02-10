package com.example.termprojectuser

import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlin.collections.ArrayList

object FirebaseQueueHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("queue")

    fun getOption(): FirebaseRecyclerOptions<Queue> {
        val options = FirebaseRecyclerOptions.Builder<Queue>()
                .setQuery(queuery, Queue::class.java)
                .build()
        return options
    }

    fun writeValue(orderlist: ArrayList<Order>){
        val postValue = hashMapOf<String, Any>()
        val list = ArrayList<OrderList>()
        for (i in orderlist){
            val item = OrderList(i.item.imageUrl, i.reward, i.item.name, i.item.price, i.quantity)
            list.add(item)
        }
        list.forEach {
            postValue.put(it.name, it)
        }
        FirebaseQueueIDHelper.getCurrentQueue { currentq, date ->
            queuery.child(currentq).apply {
                child("queueId").setValue(currentq)
                child("orderList").setValue(postValue)
            }

        }
        FirebaseQueueIDHelper.updateCurrentQueue()
        FirebaseSalesHelper.updateValue(orderlist)

    }
}