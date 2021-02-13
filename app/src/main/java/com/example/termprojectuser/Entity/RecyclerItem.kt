package com.example.termprojectuser.Entity

sealed class RecyclerItem {
    data class Header(val typeName: String): RecyclerItem()
    data class Product(val order: Order): RecyclerItem()
    companion object{
        fun transformList(orderList: ArrayList<Order>):ArrayList<RecyclerItem>{
            val groupList = orderList.groupBy {
                it.reward
            }
            val myOrderList = ArrayList<RecyclerItem>()
            val myRedeemList = ArrayList<RecyclerItem>()
            for (i in groupList.keys){
                if (i){
                    myRedeemList.add(Header("Reward Order"))
                    for (v in groupList.getValue(i)){
                        myRedeemList.add(Product(v))
                    }
                }else{
                    myOrderList.add(Header("Order"))
                    for (v in groupList.getValue(i)){
                        myOrderList.add(Product(v))
                    }
                }
            }
            myOrderList.addAll(myRedeemList)


            return myOrderList
        }
    }

}