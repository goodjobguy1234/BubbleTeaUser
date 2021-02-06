package com.example.termprojectuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        val phoneid: String = "Unknown",
        var point: Int = -1
):Parcelable{
    fun checkPoint(item: RewardMenu): Boolean{
        if (item.point > point){
            return false
        }
        return true
    }
    companion object{
//        fun createUser(): ArrayList<User>{
//            return arrayListOf(
//                    User("0639489842", 100),
//                    User("123456", 500)
//            )
//        }
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
