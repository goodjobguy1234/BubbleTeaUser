package com.example.termprojectuser

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

const val REQUEST_CODE = 1
class MainActivity : AppCompatActivity() {
    private lateinit var menu: ArrayList<Menu>
    private lateinit var menu_recycleView: RecyclerView
    private lateinit var confirm_btn: Button
    private lateinit var queue_btn: Button
    private lateinit var redeem_btn: Button
    private lateinit var total_txt: TextView
    lateinit var sectionList: ArrayList<RecyclerItem>
    lateinit var userList: ArrayList<User>
    private lateinit var order_recycleView: RecyclerView
    private lateinit var order: ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        order = ArrayList()
        menu = Menu.createMenu()
        sectionList = ArrayList() // do not create main list or private variable in on Create functiion like this -> use lazy search for `android by lazy`
        userList = User.createUser()
        total_txt.text = "Total    0" // TODO should be in init view function
        val menuLayout = GridLayoutManager(this, 2)
        val orderLayout = LinearLayoutManager(this)
        menu_recycleView.apply {
            layoutManager = menuLayout
            adapter = MenuAdapter(menu) { item ->
                var position = order.size
                if (checkDuplicate(Order(item, 1, false))) {
                    order.forEach {
                        if (it.item.name == item.name && !it.reward) {
                            it.addQuantity()
                            position = order.indexOf(it)
                        }
                    }
                } else {
                    order.add(Order(item, 1, false))
                }
                sectionList.clear()
                sectionList.addAll(transformList(order))
                order_recycleView.adapter?.notifyDataSetChanged()
                order_recycleView.scrollToPosition(position)
                addMenu(item)
                total_txt.text = "Total    ${calculateTotal()}"
            }
        }

        order_recycleView.apply {
            layoutManager = orderLayout
            adapter = OrderAdapter(sectionList) { position, type ->
                when (type) {
                    1 -> {
                        cancelMenu(order[position].item, order[position].quantity)
                        order.removeAt(position)
                    }
                    2 -> {
                        order[position].addQuantity()
                        addMenu(order[position].item)
                    }
                    3 -> {
                        subtractMenu(order[position].item)
                        order[position].subtractQuantity()
                        if (order[position].quantity == 0) {
                            order.removeAt(position)
                        }
                    }

                }
                sectionList.clear()
                sectionList.addAll(transformList(order))
                order_recycleView.adapter?.notifyDataSetChanged()
                total_txt.text = "Total    ${calculateTotal()}"
            }
        }

        // TODO put everything here to initView function + use mvvm. It is useful for this case
    }


    fun init() {
        menu_recycleView = findViewById<RecyclerView>(R.id.menu_recycleview)
        order_recycleView = findViewById<RecyclerView>(R.id.order_recycleview)
        confirm_btn = findViewById(R.id.confirm_btn)
        queue_btn = findViewById(R.id.queue_btn)
        redeem_btn = findViewById(R.id.redeem_btn)
        total_txt = findViewById(R.id.total_txt)
    }

    override fun onStart() {
        super.onStart()
        setUpLayout()
    }
    fun setUpLayout(){
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        setUpLayout()
    }

    fun onQueueBtnClick(view: View) {
        val intent = Intent(this, QueueActivity::class.java)
        startActivity(intent)
    }

    fun addMenu(item: Menu) {
        val position = menu.indexOf(item)
        menu[position].subtractRemain()
    }

    fun cancelMenu(item: Menu, amount: Int) {
        val position = menu.indexOf(item)
        menu[position].addRemainAmount(amount)
    }

    fun subtractMenu(item: Menu) {
        val position = menu.indexOf(item)
        menu[position].addRemain()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList("orderlist", order)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val list = savedInstanceState.getParcelableArrayList<Order>("orderlist")
        if (list != null) {
            order = list
        }
    }

    fun onConfirmBtnClick(view: View) {
        if (order.isEmpty()) {
            Toast.makeText(this, "Please order something", Toast.LENGTH_LONG).show()
        } else {
            var dialog = createEditDialog("Input Phone ID")
            setOnClickEditDialog(dialog,{ it, _ ->
                it.dismiss()
                showDialog()
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()

            },{
                it.dismiss()
                showDialog()
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()
            })

        }
    }

    fun calculateTotal(): Int {
        var total = 0
        order.forEach {
            if(!it.reward){
                total += (it.quantity * it.item.price)
            }
        }
        return total
    }

    fun onRedeemRewardBtnClick(view: View) {
        val dialog = createEditDialog("Input Phone ID")
        setOnClickEditDialog(dialog,{ it, phoneId ->
            it.dismiss()
            val intent = Intent(this, RewardActivity::class.java)
            val position = userList.indexOf(User(phoneId, 0))
            intent.putExtra("user", userList[position])
            intent.putExtra("menulist", menu)
            startActivityForResult(intent, REQUEST_CODE)

        },{
            it.dismiss()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //After get reward item
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                val arrayitem = data!!.getParcelableArrayListExtra<Order>("item")
                val return_user = data.getParcelableExtra<User>("return_user")!!
                Log.d("returnOrder", arrayitem.toString())
                arrayitem!!.forEach { reward ->
                    if (checkDuplicate(reward)) {
                        val position = order.indexOf(reward)
                        order[position].addQuantity()
                    } else {
                        order.add(reward)
                    }
                    addMenu(reward.item)
                }
                sectionList.clear()
                sectionList.addAll(transformList(order))
                order_recycleView.adapter!!.notifyDataSetChanged()
                userList[userList.indexOf(return_user)].update(return_user)
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show()
        }

    }

    fun checkDuplicate(item: Order): Boolean {
//        order.any { it.item.name == item.name }
        return order.contains(item)
    }

    fun createEditDialog(message: String): AlertDialog {
        val view = layoutInflater.inflate(R.layout.dialog_content, null)
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(message)
            setView(view)
            setCancelable(false)
            setPositiveButton("Confirm") { _, _ ->
            }
            setNegativeButton("Cancel") { _, _ ->
            }
        }.create()
        return dialog
    }

    fun setOnClickEditDialog(dialog: AlertDialog, callbackpos: (AlertDialog, String) -> Unit,
    callbackneg: (AlertDialog) -> Unit) {
        dialog.setOnShowListener {
            val edit = dialog.findViewById<EditText>(R.id.input_edt)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.parseColor("#81B29A"))
                setOnClickListener {
                    val phoneId = edit!!.text
                    //do smt
                    if (phoneId.isNotBlank() && phoneId.isNotEmpty()) {
                        if (isUserExist(phoneId.toString())){
                            callbackpos(dialog, phoneId.toString())
                        }else{
                            edit.error = "No User"
                        }
                    }else{
                        edit.error = "Please input your id"
                    }
                }
            }
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColor(resources.getColor(R.color.button))
                setOnClickListener {
                    callbackneg(dialog)
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
            }
        }
        dialog.show()
    }

    fun isUserExist(item:String):Boolean{
        return userList.any { it.phoneId == item}
    }

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



