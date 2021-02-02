package com.example.termprojectuser

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
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

    fun showConfirmDialog(message: String){
        val dialog = AlertDialog.Builder(this).apply{
            setTitle(message)
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
    fun createNormalDialog(message: String): AlertDialog{
        return AlertDialog.Builder(this).apply {
            setTitle(message)
            setPositiveButton("Confirm"){_,_ ->

            }
            setNegativeButton("Cancel"){_,_ ->

            }
        }.create()
    }

    abstract fun getLayoutResourceId():Int
}