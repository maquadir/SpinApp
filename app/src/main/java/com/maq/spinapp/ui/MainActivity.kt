package com.maq.spinapp.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.maq.propertyapp.properties.ItemViewModelFactory
import com.maq.spinapp.R
import com.maq.spinapp.data.Item
import com.maq.spinapp.data.ItemRepository
import com.maq.spinapp.database.ItemDatabase
import com.maq.spinapp.database.Itemdao
import com.maq.spinapp.databinding.ActivityMainBinding
import com.maq.spinapp.viewmodels.ItemViewModel
import kotlinx.android.synthetic.main.custom_toast.*

class MainActivity : AppCompatActivity() {

    //region Variables
    private lateinit var dialog :Dialog
    private  lateinit var viewModel: ItemViewModel
    private lateinit var factory: ItemViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemdao: Itemdao
    var itemArray = ArrayList<String>()
    //endregion

    //region onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup databinding
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        //setup UI
        setupUI()

        //navigate to roulette activity on button click
        binding.doneButton.setOnClickListener {
            val intent = Intent(this, RouletteActivity::class.java)
            intent.putExtra("ItemArray",itemArray)
            startActivity(intent)
        }

    }
    //endregion

    //region Setup UI
    private fun setupUI() {

        //construct repository to handle data operations with database and dao
        itemdao = ItemDatabase.getDatabase(application).getItemDao()
        val repository = ItemRepository(itemdao)

        //setup view model
        factory =
            ItemViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(ItemViewModel::class.java)

        //bind layout to viewmodel
        binding.item = viewModel

        //trigger data fetch from Room database if exists
        viewModel.getItemsDb()
        viewModel.getCount()//get count from database

        //observe data fetch from Room database and display via list view
        viewModel.itemsfetch.observe(this, Observer { items ->

            itemArray.clear()
            if(items != null) {

                for (i in 0 until items.size) {
                    Log.i("Items", items[i].item)
                    itemArray.add(items[i].item)

                }
                val adapter = ArrayAdapter(this,
                    R.layout.list_view_item, itemArray)

                val listView:ListView = findViewById(R.id.list_view)
                listView.adapter = adapter
            }

        })

        //fetch data from Room database and perform databinding to layout
        viewModel.count.observe(this, Observer { items ->

            if(items != null) {
                Log.i("Items", items.toString())
            }

        })

    }
    //endregion

    //region Create a menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    //endregion

    //region Handle click events on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_add -> {
            // do stuff
            Log.d("MainActivity","List is Refreshed ")

            //check if phone is connected to internet
            showDialog()

            true

        }
        else -> super.onOptionsItemSelected(item)
    }
    //endregion

    // region Function to show input dialog
    fun showDialog( ){

        //initialize dialog and its attributes
        dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.input_dialog)
        val okButton = dialog .findViewById(R.id.btn_ok) as Button
        val cancelButton = dialog .findViewById(R.id.btn_cancel) as Button
        val note_text = dialog .findViewById(R.id.et_note) as EditText
        val dialog_title = dialog .findViewById(R.id.tv_dialog_title) as TextView

        //add dialog title
        dialog_title.text = getString(R.string.add_item)


        //add dialog attributes
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.TOP
        wmlp.y = 150

        //show dialog
        dialog .show()

        //save item to database when ok button is clicked
        okButton.setOnClickListener {

            val item = note_text.text.toString()

            if (item.equals("")) {
                displayToast("Enter a text")
            } else {

                viewModel.insertItems(Item(item))

                //trigger data fetch from Room database
                viewModel.getItemsDb()

                dialog.dismiss()
            }
        }

        //dismiss dialog on cancel button click
        cancelButton.setOnClickListener {
            dialog .dismiss()
        }

    }
    //endregion

    //region Function to display toast
    fun displayToast(message:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast = Toast(applicationContext)
        myToast.setGravity(Gravity.BOTTOM,0,200)
        myToast.view = layout
        val toastText = layout.findViewById(R.id.custom_toast_message) as TextView
        toastText.text = message

        myToast.show()
    }
    //endregion
}
