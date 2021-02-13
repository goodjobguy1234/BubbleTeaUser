package com.example.termprojectuser.FirebaseHelper

import com.example.termprojectuser.Entity.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseSalesHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("sale")
    private var currentQuantity = 0
    fun getCurrentQuantity(name:String){
        queuery.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val quantity = snapshot.child(name).child("quantity").getValue(Double::class.java)
                    currentQuantity = (quantity!!.toInt())
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    fun updateValue(orderlist: ArrayList<Order>){
        orderlist.forEach {orderitem ->
            if (!orderitem.reward){
                getCurrentQuantity(orderitem.item.name)
                queuery.child(orderitem.item.name).updateChildren(mapOf(
                    "quantity" to (orderitem.quantity + currentQuantity)
                ))
            }

        }
        }
    }