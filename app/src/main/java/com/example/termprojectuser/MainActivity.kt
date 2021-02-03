package com.example.termprojectuser

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.lang.Exception

const val REQUEST_CODE = 1
const val SUBTRACT = 0
const val ADD = 1
const val UPDATE = 2
class MainActivity : BaseActivity() {
    private lateinit var menu: FirebaseRecyclerOptions<Menu>
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
        init()
        order = ArrayList()
        menu = FIrebaseMenuHelper.getOption()
        sectionList = ArrayList()
        userList = User.createUser()
        total_txt.text = "Total    0"
        val menuLayout = GridLayoutManager(this, 2)
        val orderLayout = LinearLayoutManager(this)
        // change when connect with firebase
        menu_recycleView.apply {
            layoutManager = menuLayout
            adapter = MenuAdapter(menu) { item ->
                FIrebaseMenuHelper.updateRemain(item.name, SUBTRACT)
                delay {
                    var position = order.size
                    if (Order.checkDuplicate(Order(item, 1, false), order)) {
                        order.forEach {
                            if (it.item.name == item.name && !it.reward) {
                                it.addQuantity()
                                position = order.indexOf(it)
                            }
                        }
                    } else {
                        order.add(Order(item, 1, false))
                    }
                    order_recycleView.scrollToPosition(position)
                    fetchOrderRecycler(order, sectionList)
                }
            }

        }
// change when connect with firebase
        order_recycleView.apply {
            layoutManager = orderLayout
            adapter = OrderAdapter(sectionList) { position, type, name ->
                val item = sectionList[position]
                val mposition = order.indexOf((item as RecyclerItem.Product).order)
                when (type) {
                    1 -> {
                        FIrebaseMenuHelper.updateRemainAmount(name, order[mposition].quantity)
                            order.removeAt(mposition)

                    }
                    2 -> {
                        FIrebaseMenuHelper.updateRemain(name, SUBTRACT){
                            if (it.checkRemain()){
                                order[mposition].addQuantity()
                                fetchOrderRecycler(order, sectionList)
                            }
                        }
                        }
                    3 -> {
                        FIrebaseMenuHelper.updateRemain(name, ADD)
                            order[mposition].subtractQuantity()
                            if (order[mposition].quantity == 0) {
                                order.removeAt(mposition)
                            }
                    }
                }
                fetchOrderRecycler(order, sectionList)
            }
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }


    fun init() {
        menu_recycleView = findViewById<RecyclerView>(R.id.menu_recycleview)
        order_recycleView = findViewById<RecyclerView>(R.id.order_recycleview)
        confirm_btn = findViewById(R.id.confirm_btn)
        queue_btn = findViewById(R.id.queue_btn)
        redeem_btn = findViewById(R.id.redeem_btn)
        total_txt = findViewById(R.id.total_txt)
    }

    fun onQueueBtnClick(view: View) {
        val intent = Intent(this, QueueActivity::class.java)
        startActivity(intent)
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
            fetchOrderRecycler(order, sectionList)
        }
    }

    fun onConfirmBtnClick(view: View) {
        if (order.isEmpty()) {
            Toast.makeText(this, "Please order something", Toast.LENGTH_LONG).show()
        } else {
            var dialog = createEditDialog("Input Phone ID")
            // if accept add point, if not don't add point
            setOnClickEditDialog(dialog,{ it, _ ->
                it.dismiss()
                showConfirmDialog("Order Confirmed")
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()

            },{
                it.dismiss()
                showConfirmDialog("Order Confirmed")
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()
            })
            //push queue and order + update user point if has
        }
    }
    fun onRedeemRewardBtnClick(view: View) {
        val dialog = createEditDialog("Input Phone ID")
        setOnClickEditDialog(dialog,{ it, phoneId ->
            it.dismiss()
            val intent = Intent(this, RewardActivity::class.java)
            val position = userList.indexOf(User(phoneId, 0))
            intent.putExtra("user", userList[position])
//            intent.putExtra("menulist", menu)
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
                    if (Order.checkDuplicate(reward, order)) {
                        val position = order.indexOf(reward)
                        order[position].addQuantity()
                    } else {
                        order.add(reward)
                    }
//                    addMenu(reward.item)
                }
                sectionList.clear()
                sectionList.addAll(RecyclerItem.transformList(order))
                order_recycleView.adapter!!.notifyDataSetChanged()
                userList[userList.indexOf(return_user)].update(return_user)
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show()
        }

    }

    private fun setOnClickEditDialog(dialog: AlertDialog, callbackpos: (AlertDialog, String) -> Unit,
                                     callbackneg: (AlertDialog) -> Unit) {
        dialog.setOnShowListener {
            val edit = dialog.findViewById<EditText>(R.id.input_edt)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.parseColor("#81B29A"))
                setOnClickListener {
                    val phoneId = edit!!.text
                    //do smt
                    if (phoneId.isNotBlank() && phoneId.isNotEmpty()) {
                        if (User.isUserExist(phoneId.toString(), userList)){
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

    override fun onStart() {
        super.onStart()
        (menu_recycleView.adapter as FirebaseRecyclerAdapter<*, *>).startListening()
    }

    override fun onStop() {
        super.onStop()
        (menu_recycleView.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
    }

    fun fetchOrderRecycler(order: ArrayList<Order>, sectionlist:ArrayList<RecyclerItem>){
        sectionList.clear()
        sectionList.addAll(RecyclerItem.transformList(order))
        order_recycleView.adapter?.notifyDataSetChanged()
        total_txt.text = "Total    ${Order.calculateTotal(order)}"
    }
    fun clearOrderRecycler(order: ArrayList<Order>, sectionlist:ArrayList<RecyclerItem>){

    }
    fun delay(callback: () -> Unit){
        Handler(Looper.getMainLooper()).postDelayed({
            callback()
        }, 100)
    }
}



