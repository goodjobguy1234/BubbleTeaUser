package com.example.termprojectuser.FirebaseHelper

import com.example.termprojectuser.Entity.Order
import com.example.termprojectuser.Entity.RewardSale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseRewardHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("reward")

    fun getCurrentQuantity(name:String, callback: (Int) -> Unit){
        queuery.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val quantity = snapshot.child(name).child("quantity").getValue(Double::class.java)
                callback(quantity!!.toInt())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun updateValue(orderlist: ArrayList<Order>){
        orderlist.forEach {orderitem ->
            if (orderitem.reward){
               getCurrentQuantity("Reward ${orderitem.item.name}") { currentQuantity ->
                    writeValue(
                            RewardSale(orderitem.item.imageUrl,
                                    orderitem.item.name,
                                    orderitem.item.price,
                                    orderitem.quantity
                            ),
                            (orderitem.quantity + currentQuantity)
                    )
                }
            }
        }
    }

    fun resetRewardSalesQuantity(){
        queuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val item = it.getValue(RewardSale::class.java)
                    writeValue(item!!, 0)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun writeValue(item: RewardSale, quantity: Int){
        queuery.child("Reward ${item.name}").setValue(
                RewardSale(
                        item.imageUrl,
                        item.name,
                        item.price,
                        quantity
                )
        )
    }
}