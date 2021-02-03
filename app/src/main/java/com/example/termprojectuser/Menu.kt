package com.example.termprojectuser

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Menu(
        val imageUrl: String = "",
        val name: String = "Unknow",
        val point: Int =-1,
        val price: Int = -1,
        var remain: Int = -1
        ): Parcelable{
//        companion object{
//                fun createMenu():ArrayList<Menu>{
//                        var menu = ArrayList<Menu>()
//                        menu.add(Menu(R.drawable.yuzu_refresher, "Yuzu refresher", 35,10))
//                        menu.add(Menu(R.drawable.classic_brown_sugar_milk_tea,"Classic brown sugar milk tea", 75,10 ))
//                        menu.add(Menu(R.drawable.matcha_brown_sugar_latte, "Matcha brown sugar latte", 105,10))
//                        menu.add(Menu(R.drawable.traditional_thai_milk_tea, "Traditional Thai milk tea", 15,10))
//                        menu.add(Menu(R.drawable.hojicha_latte, "Hojicha latte", 40,10))
//                        menu.add(Menu(R.drawable.caramel_macchiato, "Caramel macchiato", 40,10))
//                        return menu
//
//                }
//        }
        fun subtractRemain(){
                remain--
        }
        fun addRemain(){
                remain++
        }
        fun addRemainAmount(amount: Int){
                remain += amount
        }
        fun checkRemain():Boolean{
                if (remain >= 1){
                        return true
                }
                return false
        }

        override fun equals(other: Any?): Boolean {
                return (other is Menu) && (name == other.name)
        }
        fun toMap(): Map<String, Any?>{
                return mapOf(
                        "imageUrl" to imageUrl,
                        "name" to name,
                        "point" to point,
                        "price" to price,
                        "remain" to remain
                )
        }
}

object MenuClassParceler: Parceler<Menu> {
        override fun create(parcel: Parcel): Menu {
                return Menu(parcel.readString()!!, parcel.readString()!!, parcel.readInt(), parcel.readInt(), parcel.readInt())
        }

        override fun Menu.write(parcel: Parcel, flags: Int) {
                parcel.writeString(imageUrl)
                parcel.writeString(name)
                parcel.writeInt(point)
                parcel.writeInt(price)
                parcel.writeInt(remain)
        }

}
