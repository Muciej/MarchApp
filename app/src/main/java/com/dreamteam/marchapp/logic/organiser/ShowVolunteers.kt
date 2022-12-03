package com.dreamteam.marchapp.logic.organiser

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import com.dreamteam.marchapp.R
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter


class ShowVolunteers : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organiser_show_volunteers)
        val backBtn = findViewById<Button>(R.id.btnBack)
        val volunteersList = arrayOf("Nie wybrano", "Tomasz Nowak", "Andrzej Jakiś", "Anna Kowalska", "Wszyscy")

        //val spinner = findViewById<Spinner>(R.id.spinner3)
        //val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, volunteersList)

        val data = arrayOf(arrayOf("Tomasz", "Nowak", "123456789", "tomhol@wp.pl"),
            arrayOf("Andrzej", "Jakiś", "123456789", "andjak@wp.pl"),
            arrayOf("Anna", "Kowalska", "123456789", "annkow@wp.pl"))

        backBtn.setOnClickListener{
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }


        var adapterData: SimpleTableDataAdapter
        val tableView = findViewById<View>(R.id.volunteersTable) as TableView<*>
        val adapterHead = SimpleTableHeaderAdapter(this@ShowVolunteers, "Imie", "Nazwisko", "telefon", "Email")
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.BLACK)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())


        var customSpinner = findViewById<TextView>(R.id.textView)

        customSpinner.setOnClickListener{
            val dialog = Dialog(this@ShowVolunteers)
            dialog.setContentView(R.layout.dialog_searchable_spinner)

            dialog.window?.setLayout(650,800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()



            var edittext = dialog.findViewById<EditText>(R.id.edit_text)
            var listview = dialog.findViewById<ListView>(R.id.listView)

            var adapter = ArrayAdapter(this@ShowVolunteers, android.R.layout.simple_list_item_1, volunteersList)


            listview.adapter = adapter

            edittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(charSequence)
                }

                override fun afterTextChanged(p0: Editable?) {}

            })



            listview.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    customSpinner.text = adapter.getItem(p2)

                    adapterData = if (listview.getItemAtPosition(p2) == "Wszyscy") SimpleTableDataAdapter(this@ShowVolunteers, data)
                    else if (listview.getItemAtPosition(p2)!="Nie wybrano") SimpleTableDataAdapter(this@ShowVolunteers, arrayOf(getInd(data,
                        listview.getItemAtPosition(p2) as String)))
                    else SimpleTableDataAdapter(this@ShowVolunteers, arrayOf())

                    adapterData.setTextSize(12)
                    adapterData.setTextColor(Color.BLACK)
                    tableView.dataAdapter = adapterData
                    dialog.dismiss()
                }

    }
}

    fun getInd(list : Array<Array<String>>, name : String ): Array<String> {
        var id = 0

        list.get(id)

        for (row in list)
        {
            if((row.contains(name.split(" ")[0])) && row.contains(name.split(" ")[1]))
            {
                return row
            }
        }
        return arrayOf()
    }
}




/*
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                adapterData = if (p2==(volunteersList.size-1)) SimpleTableDataAdapter(this@ShowVolunteers, data)
                else if (p2!= 0) SimpleTableDataAdapter(this@ShowVolunteers, arrayOf(data[p2-1]))
                else SimpleTableDataAdapter(this@ShowVolunteers, arrayOf())

                adapterData.setTextSize(12)
                adapterData.setTextColor(Color.BLACK)
                tableView.dataAdapter = adapterData
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

         */

/*
        with(spinner)
        {
            adapter = aa
            setSelection(0, false)
            prompt = "Wolontariusz"
            gravity = android.view.Gravity.CENTER
        }

         */