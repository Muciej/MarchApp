package com.dreamteam.marchapp.logic.organiser


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.R.id.*
import com.dreamteam.marchapp.R.id.nr_startowy_edit
import com.dreamteam.marchapp.R.id.wsp_edit
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.organiser.Organisatormain2
import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.NameValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_edit_point.*
import kotlinx.android.synthetic.main.dialog_edit_user.*
import kotlinx.android.synthetic.main.dialog_edit_user.edit_imie
import kotlinx.android.synthetic.main.dialog_edit_user.nazwisko_edit
import kotlinx.android.synthetic.main.dialog_edit_user.nr_startowy_edit
import kotlinx.android.synthetic.main.dialog_edit_user.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb
import java.util.*
import kotlin.collections.ArrayList


class ShowAndEditPoints : AppCompatActivity(), TableDataClickListener<Array<String>> {
    var lastClickedRow = -1
    lateinit var data : MutableList<Array<String>>
    lateinit var adapterData: SimpleTableDataAdapter
    lateinit var tableView : TableView<Array<String>>
    lateinit var editModeCheckbox : CheckBox
    lateinit var pointsList : ArrayList<String>
    var connector = JDBCConnector
    lateinit var customSpinner : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_and_edit_points)
        val backBtn = findViewById<Button>(btnBack)
        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditPoints, "Id","Kol.", "Online", "Nazwa", "Km", "Współrzędne")
        tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        tableView.addDataClickListener(this)
        tableView.setHeaderBackgroundColor(Color.rgb(		98, 0, 238))
        val colorEvenRows = Color.rgb(224,224,224)
        val colorOddRows = Color.WHITE
        customSpinner = findViewById<TextView>(textView)
        tableView.setDataRowBackgroundProvider(
            TableDataRowBackgroundProviders.alternatingRowColors(
                colorEvenRows,
                colorOddRows
            )
        )
        editModeCheckbox = findViewById<CheckBox>(editCheckbox)
        
        init_points()


        backBtn.setOnClickListener{
            lateinit var intent : Intent
            intent = Intent(this, Organisatormain2::class.java)
            startActivity(intent)
        }

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())



        val columnModel = TableColumnPxWidthModel(6, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 120)
        columnModel.setColumnWidth(2, 160)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 120)
        columnModel.setColumnWidth(5, 300)
        tableView.columnModel = columnModel



        customSpinner.setOnClickListener{
            val dialog = Dialog(this@ShowAndEditPoints)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setLayout(800,800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val edittext = dialog.findViewById<EditText>(edit_text)
            val listview = dialog.findViewById<ListView>(listView)
            val adapter = ArrayAdapter(this@ShowAndEditPoints, android.R.layout.simple_list_item_1, pointsList)

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

                    adapterData = if (listview.getItemAtPosition(p2).toString().lowercase() == "wszystkie") SimpleTableDataAdapter(this@ShowAndEditPoints, initData())
                    else if (listview.getItemAtPosition(p2).toString().lowercase()!="nie wybrano") SimpleTableDataAdapter(this@ShowAndEditPoints, selectData(listview.getItemAtPosition(p2).toString().split(" | ")[0]))
                    else SimpleTableDataAdapter(this@ShowAndEditPoints, arrayOf())
                    adapterData.setTextSize(12)
                    adapterData.setTextColor(Color.BLACK)
                    tableView.dataAdapter = adapterData
                    dialog.dismiss()
                }

    }
    }
    private fun selectData(name: String):MutableList<Array<String>>
    {

        connector.prepareQuery("SELECT * FROM ev_test_event.punkty_kontrolne WHERE nazwa = '$name';")
        connector.executeQuery()


        var temp: Vector<String>? = null
        data = mutableListOf()
        temp = connector.getRow(0,6)
        data.add(temp.toTypedArray())
        connector.closeQuery()
        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        connector.prepareQuery("SELECT * FROM ev_test_event.punkty_kontrolne")
        connector.executeQuery()
        var temp: Vector<String>? = null
        var counter = 1

        data = mutableListOf()
        while(true)
        {
            try {
//                temp = connector.getRow(counter,6)
                temp = connector.getCurrRow(6)
                connector.moveRow()
                data.add(temp.toTypedArray())
                counter++;
            }
            catch (e: Exception)
            {
                break;
            }
        }
        connector.closeQuery()
        return data
    }


    private fun init_points()
    {
        connector.startConnection()
        connector.prepareQuery("select nazwa, kolejność from punkty_kontrolne")
        connector.executeQuery()

        pointsList = ArrayList<String>()
        pointsList.add("Nie wybrano")
        var temp: Vector<String>? = null
        var counter = 1

        while(true)
        {
            try {
                temp = connector.getCurrRow(2)
                connector.moveRow()
//                temp = connector.getRow(counter,2)
                pointsList.add(temp[0] + " | " + temp[1])
                counter++;
            }
            catch (e: Exception)
            {
                break;
            }
        }
        connector.closeQuery()
        pointsList.add("Wszystkie")
    }

    private fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        connector.prepareQuery("SELECT count(nazwa) FROM punkty_kontrolne where $field = '$obj' and id!=$id;")
        connector.executeQuery()
        if (connector.getColInts(1)[0] == 0){
            return false
        }
        return true
    }


    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {

        lastClickedRow = rowIndex

        if (!editModeCheckbox.isChecked)
        {
            class MyCustomDialog: DialogFragment() {


                override fun onCreateView(
                    inflater: LayoutInflater,
                    container: ViewGroup?,
                    savedInstanceState: Bundle?
                ): View? {

                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog?.window?.setLayout(1000,1000)
                    var rootView : View = inflater.inflate(R.layout.dialog_zoom_point, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val online = rootView.findViewById<TextView>(R.id.imie)
                    val nazwa = rootView.findViewById<TextView>(R.id.nazwisko)
                    val kolejnosc = rootView.findViewById<TextView>(R.id.nr_startowy)
                    val km = rootView.findViewById<TextView>(R.id.pseudonim)
                    val wsp = rootView.findViewById<TextView>(R.id.wsp)

                    val onlineSpan = SpannableString(online?.text.toString() + (clickedData?.get(2)))
                    val nazwaSpan = SpannableString(nazwa?.text.toString() + (clickedData?.get(3)))
                    val kolejnoscSpan = SpannableString(kolejnosc?.text.toString() + (clickedData?.get(1)))
                    val kmSpan = SpannableString(km?.text.toString() + (clickedData?.get(4)))
                    val wspSpan = SpannableString(wsp?.text.toString() + (clickedData?.get(5)))

                    onlineSpan.setSpan(ForegroundColorSpan(Color.BLUE), online.text.length, onlineSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    nazwaSpan.setSpan(ForegroundColorSpan(Color.BLUE),nazwa.text.length, nazwaSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    kolejnoscSpan.setSpan(ForegroundColorSpan(Color.BLUE), kolejnosc.text.length, kolejnoscSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    kmSpan.setSpan(ForegroundColorSpan(Color.BLUE), km.text.length, kmSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    wspSpan.setSpan(ForegroundColorSpan(Color.BLUE), wsp.text.length, wspSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    nazwa.text = nazwaSpan
                    online.text = onlineSpan
                    kolejnosc.text = kolejnoscSpan
                    km.text = kmSpan
                    wsp.text = wspSpan

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    dialog?.window?.setLayout(1000,1000)
                }
            }

            val dialog  = MyCustomDialog()
            dialog.show(supportFragmentManager, "CustomDialog")
        }

        //jesli edit_mode
        else
        {
            class MyCustomEditDialog: DialogFragment() {

                override fun onCreateView(
                    inflater: LayoutInflater,
                    container: ViewGroup?,
                    savedInstanceState: Bundle?
                ): View? {


                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    var rootView : View = inflater.inflate(R.layout.dialog_edit_point, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    rootView.editButton.setOnClickListener {

                        var editedName = edit_imie.text.toString()
                        var editedOnline = nazwisko_edit.text.toString()
                        var editedkm = nr_startowy_edit.text.toString()
                        var editedwsp = wsp_edit.text.toString()

                        var rowToEdit = 0;
                        rowToEdit = clickedData!!.get(0).toInt()
                        var iskmedited = false;



                        if (editedName.isNullOrBlank() || editedOnline.isNullOrBlank() ||
                            editedkm.isNullOrBlank() || editedwsp.isNullOrBlank()
                        ) {
                            Toast.makeText(this@ShowAndEditPoints, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                        } else {
                            var isCorrect = true

                            if (checkIfPresentInDB(editedName, "nazwa", rowToEdit ))
                            {
                                Toast.makeText(
                                    this@ShowAndEditPoints,
                                    "Punkt o podanej już istnieje w bazie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }
                            else if (checkIfPresentInDB(editedkm, "kilometr", rowToEdit))
                            {
                                iskmedited = true;
                                Toast.makeText(
                                    this@ShowAndEditPoints,
                                    "Punkt na danym kilometrze już istnieje w bazie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }
                            else if (checkIfPresentInDB(editedwsp, "współrzędne_geograficzne", rowToEdit))
                            {
                                Toast.makeText(
                                    this@ShowAndEditPoints,
                                    "Punkt o podanych współrzędnych już istnieje w bazie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }


                            if (isCorrect) {


                                connector.prepareQuery("UPDATE punkty_kontrolne SET online = ?, nazwa = ?, kilometr = ?, współrzędne_geograficzne = ? WHERE  id = ?")
                                connector.setStrVar(editedOnline, 1)
                                connector.setStrVar(editedName, 2)
                                connector.setStrVar(editedkm, 3)
                                connector.setStrVar(editedwsp, 4)
                                connector.setIntVar(rowToEdit, 5)

                                //teraz trzeba zmienic jeszcze kolejnosc

                                if (iskmedited)
                                {
                                    connector.prepareQuery("select * from punkty_kontrolne order by kilometr")
                                    connector.executeQuery()
                                    var temp: Vector<String>? = null
                                    var counter = 1

                                    data = mutableListOf()
                                    while(true)
                                    {
                                        try {
                                            temp = connector.getRow(counter,6)
                                            val kol = temp[1]
                                            connector.prepareQuery("update punkty_kontrolne set kolejność = $counter where id = $kol")
                                            connector.executeQuery()
                                            counter++;
                                        }
                                        catch (e: Exception)
                                        {
                                            break;
                                        }
                                    }
                                    iskmedited = false;
                                }



                                try{connector.executeQuery()} catch (e: Exception){print("erorr")}
                                connector.closeQuery()


                                adapterData = SimpleTableDataAdapter(this@ShowAndEditPoints, initData())
                                adapterData.setTextSize(12)
                                adapterData.setTextColor(Color.BLACK)
                                tableView.dataAdapter = adapterData
                                init_points()
                                customSpinner.text = ""
                                dismiss()

                            }
                            }

                    }

                    val name = rootView.findViewById<EditText>(R.id.edit_imie)
                    val lastname = rootView.findViewById<EditText>(R.id.nazwisko_edit)
                    val start_nr = rootView.findViewById<EditText>(R.id.nr_startowy_edit)
                    val pseudo = rootView.findViewById<EditText>(R.id.wsp_edit)

                    name.setText(clickedData?.get(3))
                    lastname.setText(clickedData?.get(2))
                    start_nr.setText(clickedData?.get(4))
                    pseudo.setText(clickedData?.get(5))

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    dialog?.window?.setLayout(1000,1000)
                }
            }

            val dialog  = MyCustomEditDialog()
            dialog.show(supportFragmentManager, "CustomEditDialog")
        }
        }
    }





