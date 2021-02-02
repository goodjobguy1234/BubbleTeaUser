package com.example.termprojectuser


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
data class Order(
        @TypeParceler<Menu, MenuClassParceler>() val item: Menu,
        var quantity: Int,
        var reward: Boolean
) : Parcelable{
        companion object{
                fun calculateTotal(order: ArrayList<Order>): Int {
                        var total = 0
                        order.forEach {
                                if(!it.reward){
                                        total += (it.quantity * it.item.price)
                                }
                        }
                        return total
                }
                fun checkDuplicate(item: Order, orderList:ArrayList<Order>): Boolean {
                        return orderList.contains(item)
                }
        }
        fun addQuantity() = quantity++
        fun subtractQuantity() = quantity--
        override fun equals(other: Any?): Boolean {
                return (other is Order) && (item.name == other.item.name) && (reward == other.reward)
        }
}
