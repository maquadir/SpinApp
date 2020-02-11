package com.maq.spinapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maq.spinapp.R
import com.maq.spinapp.SpinningWheel.SpinningWheelView
import kotlinx.android.synthetic.main.custom_toast.*


class RouletteActivity : AppCompatActivity()  {

    //region variables
    var itemArray = ArrayList<String>()
    //endregion

    //region onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roulette)

        //setup UI
        setupUI()


    }

    private fun setupUI() {

        //get extras from intent
        val intent = intent
        itemArray = intent.getStringArrayListExtra("ItemArray")

        //setup spinning wheel
        setupSpinningWheel()

    }

    private fun setupSpinningWheel() {

        val wheelView: SpinningWheelView = findViewById(R.id.wheel)

        //set items to wheel
        wheelView.items = itemArray

        // Set listener for rotation event
        wheelView.onRotationListener = object : SpinningWheelView.OnRotationListener<String?> {
            // Call once when start rotation
            override fun onRotation() {
                Log.i("Rotation", "On Rotation")
            }

            // Call once when stop rotation
            override fun onStopRotation(item: String?) {
                Log.i("Rotation", item.toString())
                displayToast(item.toString())

            }
        }

        // If true: user can rotate by touch
        // If false: user can not rotate by touch
        wheelView.isEnabled = false

        //button click to rotate wheel
        findViewById<Button>(R.id.btn_spin).setOnClickListener{
            wheelView.rotate(50f, 3000, 50)
        }
    }
    //endregion

    //region To create a menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.back_menu, menu)
        return true
    }
    //endregion

    //region To handle click events on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_back -> {
            // do stuff

            //go back to screen A
            finish()

            true

        }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion

    //region Display toast
    fun displayToast(message:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast = Toast(applicationContext)
        myToast.setGravity(Gravity.BOTTOM,0,200)
        myToast.view = layout
        val toastText = layout.findViewById(R.id.tv_message) as TextView
        toastText.text = message

        myToast.show()
    }
    //endregion
}
