package com.example.termprojectuser

import android.os.Parcel
import kotlinx.parcelize.Parceler

data class Menu(
        val imageId: Int,
        val name: String,
        val price: Int,
        var remainder: Int
        ){
        companion object{
                fun createMenu():ArrayList<Menu>{
                        var menu = ArrayList<Menu>()
                        menu.add(Menu(1234, "item1", 35,10))
                        menu.add(Menu(1234, "item2", 35,10))
                        menu.add(Menu(1234, "item3", 35,10))
                        menu.add(Menu(1234, "item4", 35,10))
                        menu.add(Menu(1234, "item5", 35,10))
                        return menu

                }
        }
        fun subtractRemain(){
                remainder--
        }
        fun addRemain(){
                remainder++
        }
        fun addRemainAmount(amount: Int){
                remainder += amount
        }
        fun checkRemain():Boolean{
                if (remainder > 0){
                        return true
                }
                return false
        }

        override fun equals(other: Any?): Boolean {
                return (other is Menu) && name == other.name
        }
}

object MenuClassParceler: Parceler<Menu> {
        override fun create(parcel: Parcel): Menu {
                return Menu(parcel.readInt(), parcel.readString()!!, parcel.readInt(), parcel.readInt())
        }

        override fun Menu.write(parcel: Parcel, flags: Int) {
                parcel.writeInt(imageId)
                parcel.writeString(name)
                parcel.writeInt(price)
                parcel.writeInt(remainder)
        }

}
