package com.example.termprojectuser


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
data class Order(
        @TypeParceler<Menu, MenuClassParceler>() val item: Menu,
        var quantity: Int
) : Parcelable{
        fun addQuantity() = quantity++
        fun subtractQuantity() = quantity--
}
