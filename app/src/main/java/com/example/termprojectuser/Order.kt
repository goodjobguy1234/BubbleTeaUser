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
        fun addQuantity() = quantity++
        fun subtractQuantity() = quantity--
        override fun equals(other: Any?): Boolean {
                return (other is Order) && (item.name == other.item.name) && (reward == other.reward)
        }
}
