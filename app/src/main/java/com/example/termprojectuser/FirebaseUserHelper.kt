package com.example.termprojectuser

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseUserHelper {
    private val firebaseInstance = FirebaseDatabase.getInstance()
    private var queuery = firebaseInstance.reference.child("user")
    fun getUser(phoneId: String, callback: (User?) -> Unit){
        queuery.orderByChild("phoneid").equalTo(phoneId).limitToFirst(1).
        addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val useritem = snapshot.child(phoneId).getValue(User::class.java)
                    callback(useritem)
                }else{
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun updateUser(phoneid: String, point:Int, currentUser: User){
        val updatePoint = currentUser.point + point
        val updatedUser = User(currentUser.phoneid, updatePoint)
        queuery.child(phoneid).setValue(updatedUser)

    }
}