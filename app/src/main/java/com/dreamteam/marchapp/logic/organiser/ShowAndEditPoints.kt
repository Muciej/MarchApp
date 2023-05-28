package com.dreamteam.marchapp.logic.organiser


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.dataclasses.CheckPoint
import com.dreamteam.marchapp.logic.shared.ShowAndEditObject
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.dialog_edit_point.*
import kotlinx.android.synthetic.main.dialog_edit_point.view.*
import kotlinx.android.synthetic.main.dialog_edit_user.view.editButton
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb


class ShowAndEditPoints : ShowAndEditObject(){
    private lateinit var pointsList: ArrayList<CheckPoint>


    //TODO: ZMIENIĆ Z PEOPLE NA COŚ INNEGO
    private fun pointUpdated(newPoints: ArrayList<CheckPoint>) {
        pointsList = newPoints
        peopleNames.clear()
        peopleNames.add("wszyscy")
        peopleNames.addAll(newPoints.map { "${it.name} | ${it.coords}" })
        peopleNames.add("nie wybrano")

        init_spinner_and_table()
    }

    override fun setDataViewModel() {
        super.setDataViewModel()
        dataViewModel.checkPoints.observe(this) { newPoints ->
            pointUpdated(newPoints)
        }
    }


    override fun setHeader() {
        val adapterHead = SimpleTableHeaderAdapter(
            this@ShowAndEditPoints,
            "Id", "Online", "Nazwa", "Km", "Współrzędne"
        )
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
    }

    //tylko organizator ma do tego dostęp
    override fun setAccesssLevel() {
        binding.customSpinner.text = "Wybierz punkt"
        binding.customSpinner.hint = "Wybierz punkt"
        binding.ChooseVolonteersTextView.text= "Wybierz punkt"
    }

    override fun goBack()
    {
        var intent =  Intent(this, Organisatormain2::class.java)
        startActivity(intent)
    }


    override fun selectData(name: String):MutableList<Array<String>>
    {
        val point: CheckPoint =
            pointsList.first { it.name == name.split(" | ")[0]}

        data.clear()
        data.add(
            arrayOf(
                point.id.toString(),
                point.online.toString(),
                point.name,
                point.dist.toString(),
                point.coords)
        )

        return data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 200)
        columnModel.setColumnWidth(2, 300)
        columnModel.setColumnWidth(3, 130)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel
    }


    override fun initData():MutableList<Array<String>>
    {
        data.clear()

        pointsList.forEach { data.add(arrayOf(
            it.id.toString(),
            it.online.toString(),
            it.name,
            it.dist.toString(),
            it.coords))}
        return data
    }


    override fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        if (field == "nazwa") return pointsList.any { point -> point.name == obj && point.id != id}
        else if (field == "kilometr") return pointsList.any { point -> point.dist == obj.toInt() && point.id != id}
        else if (field == "współrzędne_geograficzne") return pointsList.any { point -> point.coords == obj && point.id != id}

        return false
    }


    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {

        class MyCustomPointDialog: BaseDialogFragment() {
            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {

                var rootView : View = inflater.inflate(R.layout.dialog_zoom_point, container, false)
                rootView.backb.setOnClickListener { dismiss() }


                val online = rootView.findViewById<TextView>(R.id.online)
                val name = rootView.findViewById<TextView>(R.id.name)
                val km = rootView.findViewById<TextView>(R.id.km)
                val wsp = rootView.findViewById<TextView>(R.id.coordinates)

                val onlineSpan = SpannableString(online?.text.toString() + (clickedData?.get(1)))
                val nazwaSpan = SpannableString(name?.text.toString() + (clickedData?.get(2)))
                val kmSpan = SpannableString(km?.text.toString() + (clickedData?.get(3)))
                val wspSpan = SpannableString(wsp?.text.toString() + (clickedData?.get(4)))

                onlineSpan.setSpan(ForegroundColorSpan(Color.BLUE), online.text.length, onlineSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                nazwaSpan.setSpan(ForegroundColorSpan(Color.BLUE),name.text.length, nazwaSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                kmSpan.setSpan(ForegroundColorSpan(Color.BLUE), km.text.length, kmSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                wspSpan.setSpan(ForegroundColorSpan(Color.BLUE), wsp.text.length, wspSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                name.text = nazwaSpan
                online.text = onlineSpan
                km.text = kmSpan
                wsp.text = wspSpan

                return rootView
            }
        }

        class MyCustomPointEditDialog: BaseDialogFragment() {

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {

                var rootView : View = inflater.inflate(R.layout.dialog_edit_point, container, false)
                rootView.backb.setOnClickListener { dismiss() }


                val name = rootView.findViewById<EditText>(R.id.edit_name)
                val lastname = rootView.findViewById<EditText>(R.id.online_edit)
                val start_nr = rootView.findViewById<EditText>(R.id.km_edit)
                val pseudo = rootView.findViewById<EditText>(R.id.coordinates_edit)


                rootView.editButton.setOnClickListener {

                    val editedName = edit_name.text.toString()
                    val editedOnline = online_edit.text.toString()
                    val editedkm = km_edit.text.toString()
                    val editedwsp = coordinates_edit.text.toString()

                    val editedPointId: Int = clickedData!![0].toInt()

                    val isCorrect = validateEditedData(editedName, editedOnline, editedkm, editedwsp, editedPointId.toString())

                    if (isCorrect) {
                        val editedPoint = CheckPoint(editedPointId, editedOnline.toBoolean(),editedName, editedkm.toInt(), editedwsp)
                        dataViewModel.updatePoint(editedPoint)
                        binding.customSpinner.text = ""
                        dismiss()
                    }
                }


                name.setText(clickedData?.get(2))
                lastname.setText(clickedData?.get(1))
                start_nr.setText(clickedData?.get(3))
                pseudo.setText(clickedData?.get(4))

                rootView.deleteButton.setOnClickListener {

                    val pointId: Int = clickedData!![0].toInt()
                    dataViewModel.deletePoint(pointId)
                    binding.customSpinner.text = ""
                    dismiss()

                    //a jak usunać z innych tabel ?

                    /*
                    connector.prepareQuery("DELETE FROM wolontariusz_punkt WHERE id_punktu = ?")
                    connector.setIntVar(rowToEdit, 1)
                    try{connector.executeQuery()} catch (e: Exception){print("error")}
                    connector.closeQuery()

                    connector.prepareQuery("DELETE FROM punkty_online WHERE  id_punktu = ?")
                    connector.setIntVar(rowToEdit, 1)
                    try{connector.executeQuery()} catch (e: Exception){print("error")}
                    connector.closeQuery()

                    connector.prepareQuery("DELETE FROM punkty_kontrolne WHERE  id = ?")
                    connector.setIntVar(rowToEdit, 1)
                    try{connector.executeQuery()} catch (e: Exception){print("error")}
                    connector.closeQuery()

                     */
                }

                return rootView
            }

            override fun validateEditedData(vararg values: String): Boolean {

                for (value in values) {
                    if (value.isBlank()) {
                        Toast.makeText(requireContext(), "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }

                if (checkIfPresentInDB(values[0], "nazwa", values[4].toInt())) {
                    showInfo("Punkt o podanej nazwie już istnieje w bazie!")
                    return false
                }

                if (checkIfPresentInDB(values[2], "kilometr", values[4].toInt())) {
                    showInfo("Punkt na podanym kilometrze już istnieje w bazie!")
                    return false
                }

                if (checkIfPresentInDB(values[3], "współrzędne_geograficzne", values[4].toInt())) {
                    showInfo("Punkt o podanych współrzędnych już istnieje w bazie!")
                    return false
                }

                return true
            }
        }


        val dialogFragment =
            if (binding.editCheckbox.isChecked)
            {
                MyCustomPointEditDialog()
            } else {
                MyCustomPointDialog()
            }


        dialogFragment.arguments = Bundle().apply {
            putStringArray("clickedData", clickedData)
        }

        dialogFragment.show(supportFragmentManager, if (binding.editCheckbox.isChecked) "CustomEditDialog" else "CustomDialog")
    }
}





















/*package com.dreamteam.marchapp.logic.organiser


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
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
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
import kotlinx.android.synthetic.main.dialog_edit_point.*
import kotlinx.android.synthetic.main.dialog_edit_point.view.*
import kotlinx.android.synthetic.main.dialog_edit_user.view.editButton
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
        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditPoints, "Id", "Online", "Nazwa", "Km", "Współrzędne")
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



        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 180)
        columnModel.setColumnWidth(2, 350)
        columnModel.setColumnWidth(3, 140)
        columnModel.setColumnWidth(4, 350)
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
        temp = connector.getRow(0,5)
        data.add(temp.toTypedArray())
        connector.closeQuery()
        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        connector.prepareQuery("SELECT * FROM ev_test_event.punkty_kontrolne order by kilometr")
        connector.executeQuery()
        var temp: Vector<String>? = null
        var counter = 1

        data = mutableListOf()
        while(true)
        {
            try {
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
        connector.prepareQuery("select nazwa, kilometr from punkty_kontrolne order by kilometr")
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
                pointsList.add(temp[0] + " | " + temp[1] + " km")
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
                    dialog?.window?.setLayout(1000,1100)
                    var rootView : View = inflater.inflate(R.layout.dialog_zoom_point, container, false)
                    rootView.backb.setOnClickListener { dismiss() }


                    val online = rootView.findViewById<TextView>(R.id.online)
                    val name = rootView.findViewById<TextView>(R.id.name)
                    val km = rootView.findViewById<TextView>(R.id.km)
                    val wsp = rootView.findViewById<TextView>(R.id.coordinates)

                    val onlineSpan = SpannableString(online?.text.toString() + (clickedData?.get(1)))
                    val nazwaSpan = SpannableString(name?.text.toString() + (clickedData?.get(2)))
                    val kmSpan = SpannableString(km?.text.toString() + (clickedData?.get(3)))
                    val wspSpan = SpannableString(wsp?.text.toString() + (clickedData?.get(4)))

                    onlineSpan.setSpan(ForegroundColorSpan(Color.BLUE), online.text.length, onlineSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    nazwaSpan.setSpan(ForegroundColorSpan(Color.BLUE),name.text.length, nazwaSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    kmSpan.setSpan(ForegroundColorSpan(Color.BLUE), km.text.length, kmSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    wspSpan.setSpan(ForegroundColorSpan(Color.BLUE), wsp.text.length, wspSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    name.text = nazwaSpan
                    online.text = onlineSpan
                    km.text = kmSpan
                    wsp.text = wspSpan

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    val defaultDisplay = DisplayManagerCompat.getInstance(requireContext()).getDisplay(
                        Display.DEFAULT_DISPLAY
                    )
                    val displayContext = requireContext().createDisplayContext(defaultDisplay!!)
                    val width = displayContext.resources.displayMetrics.widthPixels
                    val height = displayContext.resources.displayMetrics.heightPixels

                    dialog?.window?.setLayout((width*0.95).toInt(), (height * 0.6).toInt())
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

                    rootView.deleteButton.setOnClickListener {
                        //usuwanie punktu
                        var rowToEdit = 0;
                        rowToEdit = clickedData!!.get(0).toInt()

                        connector.prepareQuery("DELETE FROM wolontariusz_punkt WHERE id_punktu = ?")
                        connector.setIntVar(rowToEdit, 1)
                        try{connector.executeQuery()} catch (e: Exception){print("error")}
                        connector.closeQuery()

                        connector.prepareQuery("DELETE FROM punkty_online WHERE  id_punktu = ?")
                        connector.setIntVar(rowToEdit, 1)
                        try{connector.executeQuery()} catch (e: Exception){print("error")}
                        connector.closeQuery()

                        connector.prepareQuery("DELETE FROM punkty_kontrolne WHERE  id = ?")
                        connector.setIntVar(rowToEdit, 1)
                        try{connector.executeQuery()} catch (e: Exception){print("error")}
                        connector.closeQuery()

                        init_points()

                        dismiss() }

                    rootView.editButton.setOnClickListener {

                        var editedName = edit_name.text.toString()
                        var editedOnline = online_edit.text.toString()
                        var editedkm = km_edit.text.toString()
                        var editedwsp = coordinates_edit.text.toString()

                        var rowToEdit = 0;
                        rowToEdit = clickedData!!.get(0).toInt()



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


                                try{connector.executeQuery()} catch (e: Exception){print("error")}
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

                    val name = rootView.findViewById<EditText>(R.id.edit_name)
                    val lastname = rootView.findViewById<EditText>(R.id.online_edit)
                    val start_nr = rootView.findViewById<EditText>(R.id.km_edit)
                    val pseudo = rootView.findViewById<EditText>(R.id.coordinates_edit)

                    name.setText(clickedData?.get(2))
                    lastname.setText(clickedData?.get(1))
                    start_nr.setText(clickedData?.get(3))
                    pseudo.setText(clickedData?.get(4))

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    val defaultDisplay = DisplayManagerCompat.getInstance(requireContext()).getDisplay(
                        Display.DEFAULT_DISPLAY
                    )
                    val displayContext = requireContext().createDisplayContext(defaultDisplay!!)
                    val width = displayContext.resources.displayMetrics.widthPixels
                    val height = displayContext.resources.displayMetrics.heightPixels

                    dialog?.window?.setLayout((width*0.95).toInt(), (height * 0.6).toInt())
                }
            }

            val dialog  = MyCustomEditDialog()
            dialog.show(supportFragmentManager, "CustomEditDialog")
        }
        }
    }



 */



