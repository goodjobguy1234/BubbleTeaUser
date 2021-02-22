package com.example.termprojectuser.FirebaseHelper

import com.example.termprojectuser.Entity.Order
import com.example.termprojectuser.Entity.Sale
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
    /*
    * it update some value
    * used setvalue to fix!!
    * */
    fun updateValue(orderlist: ArrayList<Order>){
        currentQuantity = 0
        orderlist.forEach {orderitem ->
            if (!orderitem.reward){
                getCurrentQuantity(orderitem.item.name)
                queuery.child(orderitem.item.name).updateChildren(mapOf(
                    "quantity" to (orderitem.quantity + currentQuantity)
                ))
                currentQuantity = 0
            }
            currentQuantity = 0
        }
        currentQuantity = 0
    }

    fun resetSalesQuantity(){
        queuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val item = it.getValue(Sale::class.java)
                    writeValue(item!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun writeValue(item: Sale){
        queuery.child(item.name).setValue(
                Sale(
                        item.imageUrl,
                        item.name,
                        item.price,
                        0
                )
        )
    }

}