package com.example.termprojectuser.Entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        val phoneid: String = "Unknown",
        var point: Int = -1
):Parcelable{
    fun checkPoint(item: Menu): Boolean{
        if (item.point > point){
            return false
        }
        return true
    }
    companion object{

        fun isUserExist(item:String, userList: ArrayList<User>):Boolean{
            return userList.any { it.phoneid == item}
        }

    }
    fun subtractPoint(amount:Int){
        point -= amount
    }
    fun addPoint(amount: Int){
        point += amount
    }
    fun update(user: User){
        point = user.point
    }
    override fun equals(other: Any?): Boolean {
        return (other is User) && (phoneid == other.phoneid)
    }
}
