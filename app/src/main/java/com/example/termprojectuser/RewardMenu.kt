package com.example.termprojectuser

data class RewardMenu(
        val menu: Menu,
        val point: Int
){
    companion object{
        fun createMenu(): ArrayList<RewardMenu>{
            return arrayListOf(
                    RewardMenu(Menu(1234, "item1", 35,10), 70),
                    RewardMenu(Menu(1234, "item2", 35,10), 150),
                    RewardMenu(Menu(1234, "item3", 35,10), 130),
                    RewardMenu(Menu(1234, "item4", 35,10), 110),
                    RewardMenu(Menu(1234, "item5", 35,10), 100)
            )

        }
    }

}
