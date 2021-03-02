package com.example.termprojectuser.FirebaseHelper

import com.example.termprojectuser.Entity.Menu
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*


object FIrebaseMenuHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("menu")

//    return snapshot of menu data in firebase
    fun getOption(): FirebaseRecyclerOptions<Menu> {
        val options = FirebaseRecyclerOptions.Builder<Menu>()
            .setQuery(queuery, Menu::class.java)
            .build()
        return options
    }

}