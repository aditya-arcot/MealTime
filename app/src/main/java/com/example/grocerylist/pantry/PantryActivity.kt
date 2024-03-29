package com.example.grocerylist.pantry

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylist.R
import com.example.grocerylist.SharedPreferencesFunctions

lateinit var data: ArrayList<PantryItem>
lateinit var sharedPrefs: SharedPreferences

class PantryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("custom", "Opened pantry activity")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantry)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)

        val lst = SharedPreferencesFunctions.loadPantry(sharedPrefs)
        data = if (lst != null) lst else ArrayList()

        //val adapter = PantryItemAdapter(this){ checkBoxClick() }
        val adapter = PantryItemAdapter(this)

        val recyclerView = findViewById<RecyclerView>(R.id.pantry_recycler_view)
        recyclerView.adapter = adapter

        val addButton = findViewById<Button>(R.id.pantry_add_button)
        val itemNameField = findViewById<EditText>(R.id.pantry_input_name)
        val itemInfoField = findViewById<EditText>(R.id.pantry_input_info)
        addButton.setOnClickListener{ buttonClicked(itemNameField, itemInfoField, adapter)}

    }

    private fun buttonClicked(nameField: EditText, infoField: EditText, adapter: PantryItemAdapter){
        val name = nameField.text.toString().replace("\n", " ")
        var info = infoField.text.toString().replace("\n", " ")

        if (name != ""){ //info can be blank
            nameField.setText("")
            infoField.setText("")

            if (info == ""){ info = "-" }

            data.add(PantryItem(false, name, info))
            adapter.notifyItemChanged(data.size - 1)

            SharedPreferencesFunctions.savePantry(data, sharedPrefs)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}