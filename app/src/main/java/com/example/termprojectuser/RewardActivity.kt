package com.example.termprojectuser

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RewardActivity : AppCompatActivity() {
    lateinit var phone_txt: TextView
    lateinit var point_txt: TextView
    lateinit var reward_adapter: RecyclerView
    lateinit var rewardMenu: ArrayList<RewardMenu>
    lateinit var rewardOrder: ArrayList<Order>
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        init()
        rewardOrder = ArrayList()
        user = intent.getParcelableExtra<User>("user")!!
        rewardMenu = RewardMenu.createMenu()
        phone_txt.text = user.phoneId
        point_txt.text = user.point.toString()
        reward_adapter.apply {
            layoutManager = GridLayoutManager(this@RewardActivity, 2)
            adapter = RewardAdapter(rewardMenu){ item, position ->
                showDialog(item)

            }
        }
    }


    override fun onStart() {
        super.onStart()
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    fun onClickBack(view: View) {
        val intent = intent.putExtra("item", rewardOrder)
        setResult(RESULT_OK, intent)
        finish()
    }
    fun init(){
        point_txt = findViewById(R.id.point_txt)
        phone_txt = findViewById(R.id.id_txt)
        reward_adapter = findViewById(R.id.redeem_recycler)
    }

    fun showDialog(item:RewardMenu){

        val dialog = AlertDialog.Builder(this).apply {
            setTitle("Do you want to buy ${item.menu.name}?")
            setPositiveButton("Confirm"){_,_ ->

            }
            setNegativeButton("Cancel"){_,_ ->

            }
        }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setOnClickListener {
                    if (user.checkPoint(item)){
                        user.subtractPoint(item.point)
                        point_txt.text = user.point.toString()
                        dialog.dismiss()
                        showDialog()
                        addRewardOrder(item)
                    }else{
                        dialog.dismiss()
                        val seconddialog = AlertDialog.Builder(this@RewardActivity).apply {
                            setTitle("Not enough Point")
                            setPositiveButton("Confirm"){_,_ ->
                            }

                        }.create()
                        seconddialog.setOnShowListener {
                            seconddialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#81B29A"))
                        }
                        seconddialog.show()
                    }
                }
            }

        }
        dialog.show()
    }

    fun showDialog(){
        val dialog = AlertDialog.Builder(this).apply{
            setTitle("Order Confirmed")
            setCancelable(false)
            setPositiveButton("Confirm"){_,_ ->

            }
        }.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.parseColor("#81B29A"))
                setOnClickListener {
                    dialog.dismiss()

                }
            }

        }
        dialog.show()

    }

    fun addRewardOrder(item:RewardMenu){
        rewardOrder.add(Order(item.menu, 1, true))
    }


}