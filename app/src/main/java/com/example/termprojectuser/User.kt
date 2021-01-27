package com.example.termprojectuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        val phoneId: String,
        var point: Int
):Parcelable{
    fun checkPoint(item: RewardMenu): Boolean{
        if (item.point > point){
            return false
        }
        return true
    }
    companion object{
        fun createUser(): ArrayList<User>{
            return arrayListOf(
                    User("0639489842", 100),
                    User("123456", 500)
            )
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
        return (other is User) && (phoneId == other.phoneId)
    }
}
