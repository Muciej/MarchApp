package com.dreamteam.marchapp.logic.organiser


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.R.id.*
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import androidx.appcompat.app.AlertDialog


class ShowVolunteers : AppCompatActivity(), TableDataClickListener<Array<String>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organiser_show_volunteers)
        val backBtn = findViewById<Button>(btnBack)
        var adapterData: SimpleTableDataAdapter
        val tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        val customSpinner = findViewById<TextView>(textView)
        val adapterHead = SimpleTableHeaderAdapter(this@ShowVolunteers, "Imie", "Nazwisko", "telefon", "Email")
        tableView.addDataClickListener(this)


        //tu będzie leciało zapytanie do bazy zby pobrać dane wolontariuszy
        val volunteersList = arrayOf("Nie wybrano", "Nowak Tomasz", "Jakiś Andrzej", "Kowalska Anna", "Wszyscy")
        val data = arrayOf(arrayOf("Tomasz", "Nowak", "123456789", "tomhol@wp.pl"),
            arrayOf("Andrzej", "Jakiś", "123456789", "andjak@wp.pl"),
            arrayOf("Anna", "Kowalska", "123456789", "annkow@wp.pl"))

        backBtn.setOnClickListener{
            val Intent = Intent(this, OrganisatorMain::class.java)
            startActivity(Intent)
        }

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.BLACK)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())

        customSpinner.setOnClickListener{
            val dialog = Dialog(this@ShowVolunteers)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setLayout(650,800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val edittext = dialog.findViewById<EditText>(edit_text)
            val listview = dialog.findViewById<ListView>(listView)
            val adapter = ArrayAdapter(this@ShowVolunteers, android.R.layout.simple_list_item_1, volunteersList)

            listview.adapter = adapter

            edittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(charSequence)
                }
                override fun afterTextChanged(p0: Editable?) {}})


            listview.onItemClickListener =
                AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                    customSpinner.text = adapter.getItem(p2)

                    adapterData = if (listview.getItemAtPosition(p2).toString().lowercase() == "wszyscy") SimpleTableDataAdapter(this@ShowVolunteers, data)
                    else if (listview.getItemAtPosition(p2).toString().lowercase()!="nie wybrano") SimpleTableDataAdapter(this@ShowVolunteers, arrayOf(getData(data, listview.getItemAtPosition(p2) as String)))
                    else SimpleTableDataAdapter(this@ShowVolunteers, arrayOf())
                    adapterData.setTextSize(12)
                    adapterData.setTextColor(Color.BLACK)
                    tableView.dataAdapter = adapterData
                    dialog.dismiss()
                }

    }
    }


    //funkcja zwracająca odpowiedni wiersz tabeli (potrzebne ponieważ podczas dopasowywania wzorców zmienia się
    // wielkość listy a co za tym idzie także id)
    private fun getData(list : Array<Array<String>>, name : String ): Array<String> {
        val data = name.split(" ")
        for (row in list)
        {
            if((row.contains(data[0])) && row.contains(data[1]))
            {
                return row
            }
        }
        return arrayOf()
    }



    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {
        val dialog = Dialog(this@ShowVolunteers)

        dialog.setContentView(R.layout.dialog_zoom_data)
        dialog.show()

        val name = dialog.findViewById<TextView>(imie)
        val lastname = dialog.findViewById<TextView>(nazwisko)
        val phone = dialog.findViewById<TextView>(tel)
        val mail = dialog.findViewById<TextView>(mail)
        val buttonback = findViewById<Button>(backbutton)

        val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(0)))
        val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(1)))
        val phoneSpan = SpannableString(phone?.text.toString() + (clickedData?.get(2)))
        val mailSpan = SpannableString(mail?.text.toString() + (clickedData?.get(3)))


        nameSpan.setSpan(ForegroundColorSpan(Color.BLUE), name.text.length, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        lastnameSpan.setSpan(ForegroundColorSpan(Color.BLUE), lastname.text.length, lastnameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        phoneSpan.setSpan(ForegroundColorSpan(Color.BLUE), phone.text.length, phoneSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mailSpan.setSpan(ForegroundColorSpan(Color.BLUE), mail.text.length, mailSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        name.text = nameSpan
        lastname.text = lastnameSpan
        phone.text = phoneSpan
        mail.text = mailSpan


        dialog.window?.setLayout(1000,900)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        //nie wiem dlaczego to nie działa, próbowałem na kilka innych sposobów i się poddaje
        //ale jeśli chcesz wyjść to wstarczy odkliknąć
        buttonback.setOnClickListener {
            if (dialog.isShowing)
            {
            dialog.cancel()
            dialog.cancel()}
        }

    }
}