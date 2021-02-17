package com.example.termprojectuser.FirebaseHelper

import com.example.termprojectuser.ADD
import com.example.termprojectuser.Entity.Menu
import com.example.termprojectuser.SUBTRACT
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*


object FIrebaseMenuHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("menu")
    fun getOption(): FirebaseRecyclerOptions<Menu> {
        val options = FirebaseRecyclerOptions.Builder<Menu>()
            .setQuery(queuery, Menu::class.java)
            .build()
        return options
    }
//    fun updateRemain(menuname: String, TYPE: Int, callback: ((Menu?) -> Unit)? = null){
//        queuery.orderByKey().equalTo(menuname).limitToFirst(1)
//                .addListenerForSingleValueEvent(object : ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                       snapshot.children.first().getValue(Menu::class.java).let{
//                           if (!it!!.checkRemain() && TYPE == SUBTRACT){
//                               callback?.invoke(null)
//                           }
//                           if(it.checkRemain() && TYPE == SUBTRACT){
//                               callback?.invoke(it)
//                               it.subtractRemain()
//                           }
//                           if (TYPE == ADD){
//                               it.addRemain()
//                           }
//                           it.toMap()
//                       }.run {
//                           queuery.child(menuname).setValue(this)
//                       }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//
//                })
//    }

//    fun updateRemainAmount(menuname:String, amount:Int){
//        val queue = queuery.orderByKey().equalTo(menuname).limitToFirst(1)
//        queue.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (i in snapshot.children){
//                    val menu = i.getValue(Menu::class.java)
//                    menu!!.addRemainAmount(amount)
//                    queuery.child(menuname).setValue(menu.toMap())
//                }
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//    }

    }