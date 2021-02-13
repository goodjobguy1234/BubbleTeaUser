package com.example.termprojectuser

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.termprojectuser.Adapter.RewardAdapter
import com.example.termprojectuser.Entity.Menu
import com.example.termprojectuser.Entity.Order
import com.example.termprojectuser.Entity.User
import com.example.termprojectuser.FirebaseHelper.FIrebaseMenuHelper
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class RewardActivity : BaseActivity() {
    lateinit var phone_txt: TextView
    lateinit var point_txt: TextView
    lateinit var reward_recycler: RecyclerView
    lateinit var rewardMenu: FirebaseRecyclerOptions<Menu>
    lateinit var rewardOrder: ArrayList<Order>
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        init()
        rewardOrder = ArrayList()
        user = intent.getParcelableExtra<User>("user")!!
        rewardMenu = FIrebaseMenuHelper.getOption()
        phone_txt.text = user.phoneid
        point_txt.text = user.point.toString()
        reward_recycler.apply {
            layoutManager = GridLayoutManager(this@RewardActivity, 2)
            adapter = RewardAdapter(rewardMenu){ item ->
                if(item.checkRemain()){
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
        reward_recycler = findViewById(R.id.redeem_recycler)
    }

    fun showDialog(item: Menu){
        val dialog = createNormalDialog("Do you want to buy ${item.name}?")
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setOnClickListener {
                    if (user.checkPoint(item)){
                        user.subtractPoint(item.point)
                        point_txt.text = user.point.toString()
                        dialog.dismiss()
                        showConfirmDialog("Order Confirmed")
                        addRewardOrder(item)
                        FIrebaseMenuHelper.updateRemain(item.name, SUBTRACT)
                    }else{
                        dialog.dismiss()
                        showConfirmDialog("Not enough Point")
                    }

                    }
                }
            }
            dialog.show()
        }


    fun addRewardOrder(item: Menu){
        rewardOrder.add(Order(item, 1, true))
    }

    override fun onStart() {
        super.onStart()
        setUpLayout()
        (reward_recycler.adapter as FirebaseRecyclerAdapter<*, *>).startListening()
    }

    override fun onStop() {
        super.onStop()
        (reward_recycler.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
    }
}
