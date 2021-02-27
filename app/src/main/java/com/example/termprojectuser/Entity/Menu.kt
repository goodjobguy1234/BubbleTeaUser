package com.example.termprojectuser.Entity

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
        val imageUrl: String = "",
        val name: String = "Unknow",
        val point: Int =-1,
        val price: Int = -1
        ): Parcelable{

        override fun equals(other: Any?): Boolean {
                return (other is Menu) && (name == other.name)
        }
        fun toMap(): Map<String, Any?>{
                return mapOf(
                        "imageUrl" to imageUrl,
                        "name" to name,
                        "point" to point,
                        "price" to price,
                )
        }
}

object MenuClassParceler: Parceler<Menu> {
        override fun create(parcel: Parcel): Menu {
                return Menu(parcel.readString()!!, parcel.readString()!!, parcel.readInt(), parcel.readInt())
        }

        override fun Menu.write(parcel: Parcel, flags: Int) {
                parcel.writeString(imageUrl)
                parcel.writeString(name)
                parcel.writeInt(point)
                parcel.writeInt(price)

        }

}
