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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.termprojectuser.Adapter.MenuAdapter
import com.example.termprojectuser.Adapter.OrderAdapter
import com.example.termprojectuser.Entity.Menu
import com.example.termprojectuser.Entity.Order
import com.example.termprojectuser.Entity.RecyclerItem
import com.example.termprojectuser.Entity.User
import com.example.termprojectuser.FirebaseHelper.*
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val REQUEST_CODE = 1
class MainActivity : BaseActivity() {
    private lateinit var menu: FirebaseRecyclerOptions<Menu>
    private lateinit var menu_recycleView: RecyclerView
    private lateinit var confirm_btn: Button
    private lateinit var queue_btn: Button
    private lateinit var redeem_btn: Button
    private lateinit var total_txt: TextView
    private lateinit var queue_txt: TextView
    private lateinit var progressBar: ProgressBar
    lateinit var sectionList: ArrayList<RecyclerItem>
    private lateinit var order_recycleView: RecyclerView
    private lateinit var order: ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        order = ArrayList()
        menu = FIrebaseMenuHelper.getOption()
        sectionList = ArrayList()
        total_txt.text = "Total    0"
        val menuLayout = GridLayoutManager(this, 2)
        val orderLayout = LinearLayoutManager(this)

        /* setup menu list to display. also, set up action when users click add button
        it will add to order and display in order summary section
        * */
        menu_recycleView.apply {
            layoutManager = menuLayout

            adapter = MenuAdapter(menu, progressBar, menu_recycleView) { item ->
                item?.let {menu ->
                        var position = order.size
                        if (Order.checkDuplicate(Order(menu, 1, false), order)) {
                            order.forEach {
                                if (it.item.name == menu.name && !it.reward) {
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

/* for display order summary ui (when user click add menu from menu list)
        * */
        order_recycleView.apply {
            layoutManager = orderLayout
            adapter = OrderAdapter(sectionList) { position, type, name ->
                val item = sectionList[position]
                val mposition = order.indexOf((item as RecyclerItem.Product).order)

                when (type) {
                    1 -> order.removeAt(mposition)
                    2 -> order[mposition].addQuantity()
                    3 -> {
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

    //    setup ui
    override fun getLayoutResourceId() = R.layout.activity_main

//    connect ui view with variable
    private fun init() {
        progressBar = findViewById(R.id.mprogressbar)
        menu_recycleView = findViewById(R.id.menu_recycleview)
        order_recycleView = findViewById(R.id.order_recycleview)
        confirm_btn = findViewById(R.id.confirm_btn)
        queue_btn = findViewById(R.id.queue_btn)
        redeem_btn = findViewById(R.id.redeem_btn)
        total_txt = findViewById(R.id.total_txt)
        queue_txt = findViewById(R.id.txt_queue)
    }

//    when user click view queue button then system will go to queue pages
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

/*  user click confirm the order; system check if the order are empty or not
*  if yes will show text "please order something" , if no then will create dialog
* asking for input phone ID to get point that calculate from buying drink (2 drink per 3 point)
*point from drink will add to user acc. in case that user input and accept
* */
    fun onConfirmBtnClick(view: View) {
        if (order.isEmpty()) showToast(this,"Please order something" )

        else {
            val dialog = createEditDialog("Input Phone ID to retrive ${calculatePoint(order)} point")

            setOnClickEditDialog(dialog,{ it, phoneid, user ->
//                when user click accept
                it.dismiss()
                showConfirmDialog("Order Confirmed")
                FirebaseQueueHelper.writeValue(order, queue_txt)
                val point = calculatePoint(order)
                FirebaseUserHelper.updateUser(phoneid, point, user)
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()

            },{
//                when user click cancel
                it.dismiss()
                showConfirmDialog("Order Confirmed")
                FirebaseQueueHelper.writeValue(order, queue_txt)
                order.clear()
                sectionList.clear()
                total_txt.text = "Total     0"
                order_recycleView.adapter!!.notifyDataSetChanged()
            })
        }
    }

    /*  user click Redeem rewards then it will pop dialog asking for input an phone number
    that registered in database. if it database doesn't have their phone number it will prompt
    error to tell that there isn't number in database. if there is number, then it will go to reward page
* */
    fun onRedeemRewardBtnClick(view: View) {
        val dialog = createEditDialog("Input Phone ID")

        setOnClickEditDialog(dialog,{ it, phoneId, user ->
            it.dismiss()
            val intent = Intent(this, RewardActivity::class.java)
            intent.putExtra("user", user)
            startActivityForResult(intent, REQUEST_CODE)

        },{
            it.dismiss()
        })
    }

    /*  this function is for get data from reward page back and put in order summary section
    data from reward page are reward orders and user information (point and phone number)
 */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //After get reward item
        try {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                val arrayitem = data!!.getParcelableArrayListExtra<Order>("item")
                val return_user = data.getParcelableExtra<User>("return_user")!!

                arrayitem!!.forEach { reward ->
                    if (Order.checkDuplicate(reward, order)) {
                        val position = order.indexOf(reward)
                        order[position].addQuantity()
                    } else {
                        order.add(reward)
                    }
                }
                sectionList.clear()
                sectionList.addAll(RecyclerItem.transformList(order))
                order_recycleView.adapter!!.notifyDataSetChanged()
                FirebaseUserHelper.updateUser(return_user.phoneid, 0, return_user)
            }

        } catch (ex: Exception) {
            showToast(this, ex.toString())
        }
    }

    /*  setup dialog behavior*/
    private fun setOnClickEditDialog(dialog: AlertDialog,
                                     callbackpos: (AlertDialog, String, User) -> Unit,
                                     callbackneg: (AlertDialog) -> Unit) {

        dialog.setOnShowListener {
            val edit = dialog.findViewById<EditText>(R.id.input_edt)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.parseColor("#81B29A"))

                setOnClickListener {
                    val phoneId = edit!!.text

                    if (phoneId.isNotBlank() && phoneId.isNotEmpty()) {

                        FirebaseUserHelper.getUser(phoneId.toString()){
                            if (it != null) callbackpos(dialog, phoneId.toString(), it)
                            else edit.error = "No User"
                        }

                    } else edit.error = "Please input your id"
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
        setUpLayout()
        (menu_recycleView.adapter as FirebaseRecyclerAdapter<*, *>).startListening()
        setQueue()
    }

    override fun onStop() {
        super.onStop()
        (menu_recycleView.adapter as FirebaseRecyclerAdapter<*, *>).stopListening()
    }

//    clear order summary section and total text
    fun fetchOrderRecycler(order: ArrayList<Order>, sectionList:ArrayList<RecyclerItem>){
        sectionList.clear()
        sectionList.addAll(RecyclerItem.transformList(order))
        order_recycleView.adapter?.notifyDataSetChanged()
        total_txt.text = "Total    ${Order.calculateTotal(order)}"
    }

    /*  this function is called when user press confirm and all order in order summary section
    will be calculated into point
*/
    private fun calculatePoint(orderlist: ArrayList<Order>): Int {
        var totalItem = 0
        val nonRewardList = orderlist.filter {
            !it.reward
        }

        nonRewardList.forEach{
            totalItem += it.quantity
        }
        return (totalItem /2) * 3
    }

    /*  function that used to setup queue in firebase and application
   */
    fun setQueue(){
        FirebaseQueueIDHelper.getRealtimeCurrentQueue{ queueid, date ->
            val currentDate = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(Date())
            Log.d("currentDate", currentDate)
            Log.d("date", date)

            if (!date.equals(currentDate)){
                FirebaseQueueIDHelper.setQueue("A100", currentDate)
                FirebaseQueueHelper.resetValue()
                FirebaseSalesHelper.resetSalesQuantity()
                FirebaseRewardHelper.resetRewardSalesQuantity()
            }

            queue_txt.text = queueid
        }
    }
}



