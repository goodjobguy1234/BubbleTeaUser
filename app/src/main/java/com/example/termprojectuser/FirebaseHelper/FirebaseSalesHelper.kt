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

    /*get current quantity of sales data of that name item*/
    fun getCurrentQuantity(name:String, callback: (Int) -> Unit){
        queuery.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val quantity = snapshot.child(name).child("quantity").getValue(Double::class.java)
                   callback(quantity!!.toInt())
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    /*update sales quantity in the list of order*/
    fun updateValue(orderlist: ArrayList<Order>){
        orderlist.forEach {orderitem ->
            if (!orderitem.reward){
                getCurrentQuantity(orderitem.item.name){currentQuantity ->
                    writeValue(
                            Sale(orderitem.item.imageUrl,
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

    /*reset sales quantity of all item in firebase*/
    fun resetSalesQuantity(){
        queuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val item = it.getValue(Sale::class.java)
                    writeValue(item!!, 0)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    /*use to write value in firebase*/
    fun writeValue(item: Sale, quantity: Int){
        queuery.child(item.name).setValue(
                Sale(
                        item.imageUrl,
                        item.name,
                        item.price,
                        quantity
                )
        )
    }

}