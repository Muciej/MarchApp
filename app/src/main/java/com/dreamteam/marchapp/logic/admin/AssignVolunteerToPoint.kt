package com.dreamteam.marchapp.logic.admin

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
import com.dreamteam.marchapp.database.JDBCConnector
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_change_point.*
import kotlinx.android.synthetic.main.dialog_change_point.view.*
import kotlinx.android.synthetic.main.dialog_change_point.view.backb
import kotlinx.android.synthetic.main.dialog_zoom_data.view.*
import java.util.Vector


class AssignVolunteerToPoint : AppCompatActivity(), TableDataClickListener<Array<String>>,
    AdapterView.OnItemSelectedListener {
    var lastClickedRow = -1
    lateinit var data : MutableList<Array<String>>
    lateinit var adapterData: SimpleTableDataAdapter
    lateinit var tableView : TableView<Array<String>>
    lateinit var volunteersList : ArrayList<String>
    var connector = JDBCConnector
    lateinit var customSpinner : TextView
    var marches = Vector<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_point_to_volunteer)

        val backBtn = findViewById<Button>(btnBack)
        val colorEvenRows = Color.rgb(224,224,224)
        val colorOddRows = Color.WHITE
        customSpinner = findViewById<TextView>(textView)

        val adapterHead = SimpleTableHeaderAdapter(this@AssignVolunteerToPoint, "Id", "Imie", "Nazwisko", "punkt")

        tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        tableView.addDataClickListener(this)
        tableView.setHeaderBackgroundColor(Color.rgb(		98, 0, 238))

        tableView.setDataRowBackgroundProvider(
            TableDataRowBackgroundProviders.alternatingRowColors(
                colorEvenRows,
                colorOddRows
            )
        )

        init_volunteers()

        backBtn.setOnClickListener{
            var intent : Intent = Intent(this, AdministratorMain::class.java)
            startActivity(intent)
        }

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())


        val columnModel = TableColumnPxWidthModel(4, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 300)
        columnModel.setColumnWidth(2, 400)
        columnModel.setColumnWidth(4, 120)
        tableView.columnModel = columnModel

        customSpinner.setOnClickListener{
            val dialog = Dialog(this@AssignVolunteerToPoint)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setLayout(800,800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val edittext = dialog.findViewById<EditText>(edit_text)
            val listview = dialog.findViewById<ListView>(listView)
            val adapter = ArrayAdapter(this@AssignVolunteerToPoint, android.R.layout.simple_list_item_1, volunteersList)

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

                    adapterData = if (listview.getItemAtPosition(p2).toString().lowercase() == "wszyscy") SimpleTableDataAdapter(this@AssignVolunteerToPoint, initData())
                    else if (listview.getItemAtPosition(p2).toString().lowercase()!="nie wybrano") SimpleTableDataAdapter(this@AssignVolunteerToPoint, selectData(listview.getItemAtPosition(p2).toString()))
                    else SimpleTableDataAdapter(this@AssignVolunteerToPoint, arrayOf())
                    adapterData.setTextSize(12)
                    adapterData.setTextColor(Color.BLACK)
                    tableView.dataAdapter = adapterData
                    dialog.dismiss()
                }

    }
    }

    private fun selectData(name: String):MutableList<Array<String>>
    {
        val dataa = name.split(" ")
        var imie = dataa[0]
        var nazwisko = dataa[1]
        connector.startConnection()
        var stm = "SELECT id_osoby, imie, nazwisko, id_punktu FROM personel INNER JOIN konta ON konta.id_konta = personel.id_konta " +
                "LEFT JOIN wolontariusz_punkt ON wolontariusz_punkt.id_wolontariusza = personel.id_osoby " +
                "WHERE rola_id = 2 " +
                "and imie = '$imie' and nazwisko = '$nazwisko'"
        connector.prepareQuery(stm)
        connector.executeQuery()

        var temp: Vector<String>? = null
        data = mutableListOf()
        temp = connector.getRow(0,4)
        data.add(temp.toTypedArray())
        connector.closeQuery()
        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        connector.prepareQuery("SELECT id_osoby, imie, nazwisko, id_punktu FROM personel\n" +
                "INNER JOIN konta ON konta.id_konta = personel.id_konta\n" +
                "LEFT JOIN wolontariusz_punkt ON wolontariusz_punkt.id_wolontariusza = personel.id_osoby\n" +
                "WHERE rola_id = 2;")
        connector.executeQuery()
        var temp: Vector<String>? = null
        var counter = 1
        data = mutableListOf()
        while(true)
        {
            try {
//                temp = connector.getRow(counter,4)
                temp = connector.getCurrRow(4)
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

    private fun init_volunteers()
    {
        connector.startConnection()
        connector.prepareQuery("SELECT imie, nazwisko FROM personel\n" +
                "INNER JOIN konta ON konta.id_konta = personel.id_konta\n" +
                "WHERE rola_id = 2;")
        connector.executeQuery()

        volunteersList = ArrayList<String>()
        volunteersList.add("Nie wybrano")
        var temp: Vector<String>? = null
        var counter = 1

        while(true)
        {
            try {
//                temp = connector.getRow(counter,2)
                temp = connector.getCurrRow(2)
                connector.moveRow()
                volunteersList.add(temp[0] + " " + temp[1])
                counter++;
            }
            catch (e: Exception)
            {
                break;
            }
        }
        connector.closeQuery()
        volunteersList.add("Wszyscy")
    }

    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {
        lastClickedRow = rowIndex

        class MyCustomDialog: DialogFragment(), AdapterView.OnItemSelectedListener {
            var newPoint = 0;

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {


                getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog?.window?.setLayout(1000,1100)
                var rootView : View = inflater.inflate(R.layout.dialog_change_point, container, false)


                rootView.backb.setOnClickListener { dismiss() }
                rootView.edit_button.setOnClickListener {
                    var rowToEdit = 0;
                    rowToEdit = clickedData!!.get(0).toInt()

                    //musimy jeszcze sprawdzić czy wolontariusz ma juz przypisany punkt
                    //jesli nie (punkt null) to musimy zrobić insert
                    if (point.text.contains("brak"))
                    {
                        connector.prepareQuery("INSERT INTO wolontariusz_punkt VALUES(?,?)")
                        if (newPoint != null) {
                            connector.setIntVar(newPoint, 2)
                        }
                        if (rowToEdit != null) {
                            connector.setIntVar(rowToEdit, 1)
                        }
                    }

                    //jesli tak to update

                    else
                    {
                        connector.prepareQuery("UPDATE wolontariusz_punkt \n" +
                                "SET id_punktu = ? WHERE id_wolontariusza = ?;")
                        if (newPoint != null) {
                            connector.setIntVar(newPoint, 1)
                        }
                        if (rowToEdit != null) {
                            connector.setIntVar(rowToEdit, 2)
                        }
                    }

                    try{connector.executeQuery()} catch (e: Exception){print("erorr")}
                    connector.closeQuery()

                    adapterData = SimpleTableDataAdapter(this@AssignVolunteerToPoint, initData())
                    adapterData.setTextSize(12)
                    adapterData.setTextColor(Color.BLACK)
                    tableView.dataAdapter = adapterData
                    init_volunteers()
                    customSpinner.text = ""
                    dismiss()

                }

                val name = rootView.findViewById<TextView>(R.id.imie)
                val lastname = rootView.findViewById<TextView>(R.id.nazwisko)
                val point = rootView.findViewById<TextView>(R.id.point)
                var tmp = clickedData?.get(3)
                if (tmp.equals(null)) tmp = "brak";
                val myspinner= rootView.findViewById<Spinner>(R.id.spinner)
                connector.prepareQuery("SELECT * FROM punkty_kontrolne;")
                connector.executeQuery()
                marches = connector.getCol(2)
                val aa = ArrayAdapter(this@AssignVolunteerToPoint, android.R.layout.simple_spinner_item, marches)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                myspinner.adapter = aa
                with(myspinner)
                {
                    adapter = aa
                    setSelection(0, false)
                    onItemSelectedListener = this@MyCustomDialog
                    prompt = "Select point"
                    gravity = android.view.Gravity.CENTER
                }
                newPoint = marches[0].toInt()

                val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(1)))
                val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(2)))
                val pointSpan = SpannableString(point?.text.toString() + tmp)

                nameSpan.setSpan(ForegroundColorSpan(Color.BLUE), name.text.length, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                lastnameSpan.setSpan(ForegroundColorSpan(Color.BLUE),lastname.text.length, lastnameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                pointSpan.setSpan(ForegroundColorSpan(Color.BLUE), point.text.length, pointSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                name.text = nameSpan
                lastname.text = lastnameSpan
                point.text = pointSpan

                return rootView
            }

            override fun onStart() {
                super.onStart()
                getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                dialog?.window?.setLayout(1000,1100)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                newPoint = marches[p2].toInt()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val dialog  = MyCustomDialog()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}