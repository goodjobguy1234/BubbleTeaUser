package com.example.termprojectuser

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RewardActivity : BaseActivity() {
    lateinit var phone_txt: TextView
    lateinit var point_txt: TextView
    lateinit var reward_adapter: RecyclerView
    lateinit var rewardMenu: ArrayList<RewardMenu>
    lateinit var rewardOrder: ArrayList<Order>
    lateinit var user: User
    lateinit var menu:ArrayList<Menu>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        init()
        rewardOrder = ArrayList()
        user = intent.getParcelableExtra<User>("user")!!
        menu = intent.getParcelableArrayListExtra<Menu>("menulist")!!
//        rewardMenu = RewardMenu.createMenu()
        phone_txt.text = user.phoneid
        point_txt.text = user.point.toString()
        reward_adapter.apply {
            layoutManager = GridLayoutManager(this@RewardActivity, 2)
            adapter = RewardAdapter(rewardMenu){ item, position ->
                if(checkRemain(item.menu)){
                    showDialog(item)
                }else{
                    Toast.makeText(this@RewardActivity, "This menu is unavaliable", Toast.LENGTH_LONG).show()
                }

            }
        }
    }


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_reward
    }

    fun onClickBack(view: View) {

        val intent = intent.apply {
            putExtra("item", rewardOrder)
            putExtra("return_user", user)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
    fun init(){
        point_txt = findViewById(R.id.point_txt)
        phone_txt = findViewById(R.id.id_txt)
        reward_adapter = findViewById(R.id.redeem_recycler)
    }

    fun showDialog(item:RewardMenu){
        val dialog = createNormalDialog("Do you want to buy ${item.menu.name}?")
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setOnClickListener {
                    if (user.checkPoint(item)){
                        user.subtractPoint(item.point)
                        point_txt.text = user.point.toString()
                        dialog.dismiss()
                        showConfirmDialog("Order Confirmed")
                        addRewardOrder(item)
                        val position = menu.indexOf(item.menu)
//                        menu[position].subtractRemain()

                    }else{
                        dialog.dismiss()
                        showConfirmDialog("Not enough Point")
                    }

                    }
                }
            }
            dialog.show()
        }

    

    fun addRewardOrder(item:RewardMenu){
        rewardOrder.add(Order(item.menu, 1, true))
    }

    fun checkRemain(itemMenu: Menu):Boolean{
        val position = menu.indexOf(itemMenu)
        return (menu[position].remain > 0)
    }
}
