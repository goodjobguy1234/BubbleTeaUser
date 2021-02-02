package com.example.termprojectuser

sealed class RecyclerItem {
    data class Header(val typeName: String): RecyclerItem()
    data class Product(val order:Order): RecyclerItem()
    companion object{
        fun transformList(orderList: ArrayList<Order>):ArrayList<RecyclerItem>{
            val groupList = orderList.groupBy {
                it.reward
            }
            val myOrderList = ArrayList<RecyclerItem>()
            val myRedeemList = ArrayList<RecyclerItem>()
            for (i in groupList.keys){
                if (i){
                    myRedeemList.add(RecyclerItem.Header("Reward Order"))
                    for (v in groupList.getValue(i)){
                        myRedeemList.add(RecyclerItem.Product(v))
                    }
                }else{
                    myOrderList.add(RecyclerItem.Header("Order"))
                    for (v in groupList.getValue(i)){
                        myOrderList.add(RecyclerItem.Product(v))
                    }
                }
            }
            myOrderList.addAll(myRedeemList)


            return myOrderList
        }
    }

}