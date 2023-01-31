package com.dreamteam.marchapp.logic.shared


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
import com.dreamteam.marchapp.database.JDBCConnector
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.NameValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_edit_user.*
import kotlinx.android.synthetic.main.dialog_edit_user.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb
import java.util.*
import kotlin.collections.ArrayList


//doodać oba scrolle
class ShowAndEditParticipant : AppCompatActivity(), TableDataClickListener<Array<String>> {
    var lastClickedRow = -1
    lateinit var data : MutableList<Array<String>>
    lateinit var adapterData: SimpleTableDataAdapter
    lateinit var tableView : TableView<Array<String>>
    lateinit var editModeCheckbox : CheckBox
    lateinit var volunteersList : ArrayList<String>
    var connector = JDBCConnector
    lateinit var customSpinner : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organiser_show_volunteers)
        var accessLevel = intent.getStringExtra("accessLevel")
        val backBtn = findViewById<Button>(btnBack)
        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditParticipant, "Id","Nr", "Imie", "Nazwisko", "Pseudonim")
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
        if (accessLevel == "Organiser" || accessLevel == "Volunteer")
        {
            editModeCheckbox.isClickable=false
            editModeCheckbox.visibility=View.INVISIBLE
        }

        val title = findViewById<TextView>(R.id.ChooseVolonteersTextView)
        title.text = "Wybierz uczestnika"

        val hint = findViewById<TextView>(R.id.textView)
        hint.text = "Wybierz uczestnika"

        init_volunteers()


        backBtn.setOnClickListener{
            lateinit var intent : Intent
            if (accessLevel == "Organiser") intent = Intent(this, OrganisatorMain::class.java)
            else if (accessLevel == "Admin") intent = Intent(this, AdministratorMain::class.java)
            else if (accessLevel == "Volunteer") intent = Intent(this, VolunteerMain::class.java)
            startActivity(intent)
        }

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())



        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 100)
        columnModel.setColumnWidth(2, 250)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel



        customSpinner.setOnClickListener{
            val dialog = Dialog(this@ShowAndEditParticipant)
            dialog.setContentView(R.layout.dialog_searchable_spinner)
            dialog.window?.setLayout(800,800)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val edittext = dialog.findViewById<EditText>(edit_text)
            val listview = dialog.findViewById<ListView>(listView)
            val adapter = ArrayAdapter(this@ShowAndEditParticipant, android.R.layout.simple_list_item_1, volunteersList)

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

                    adapterData = if (listview.getItemAtPosition(p2).toString().lowercase() == "wszyscy") SimpleTableDataAdapter(this@ShowAndEditParticipant, initData())
                    else if (listview.getItemAtPosition(p2).toString().lowercase()!="nie wybrano") SimpleTableDataAdapter(this@ShowAndEditParticipant, selectData(listview.getItemAtPosition(p2).toString()))
                    else SimpleTableDataAdapter(this@ShowAndEditParticipant, arrayOf())
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
        connector.prepareQuery("select id_konta, nr_startowy,imie, nazwisko, pseudonim from uczestnicy  where imie='" + dataa[0] + "' and nazwisko = '" + dataa[1] + "';")
        connector.executeQuery()


        var temp: Vector<String>? = null
        data = mutableListOf()
        temp = connector.getRow(0,5)
        println("eloelo")
        println(temp)
        data.add(temp.toTypedArray())
        connector.closeQuery()
        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        connector.prepareQuery("select id_konta, nr_startowy,imie, nazwisko, pseudonim from uczestnicy;")
        connector.executeQuery()
        var temp: Vector<String>? = null
        var counter = 1

        //należy zainicjalizować
        data = mutableListOf()
        while(true)
        {
            try {
                temp = connector.getCurrRow(5)
                connector.moveRow()
//                temp = connector.getRow(counter,5)
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

    private fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        connector.prepareQuery("SELECT count(id_konta) FROM uczestnicy where $field = '$obj' and id_konta!=$id;")
        connector.executeQuery()
        //tzn ze nikt w bazie (poza obecna osoba) nie ma takiego pola
        if (connector.getColInts(1)[0] == 0){
            return false
        }
        return true
    }


    private fun init_volunteers()
    {
        connector.startConnection()
        connector.prepareQuery("select imie, nazwisko from uczestnicy")
        connector.executeQuery()

        volunteersList = ArrayList<String>()
        volunteersList.add("Nie wybrano")
        var temp: Vector<String>? = null
        var counter = 1

        while(true)
        {
            try {
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
                    dialog?.window?.setLayout(1000,900)
                    var rootView : View = inflater.inflate(R.layout.dialog_zoom_data_user, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val name = rootView.findViewById<TextView>(R.id.imie)
                    val lastname = rootView.findViewById<TextView>(R.id.nazwisko)
                    val start_nr = rootView.findViewById<TextView>(R.id.nr_startowy)
                    val pseudo = rootView.findViewById<TextView>(R.id.pseudonim)

                    val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(2)))
                    val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(3)))
                    val start_nrSpan = SpannableString(start_nr?.text.toString() + (clickedData?.get(1)))
                    val pseudoSpan = SpannableString(pseudo?.text.toString() + (clickedData?.get(4)))

                    nameSpan.setSpan(ForegroundColorSpan(Color.BLUE), name.text.length, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    lastnameSpan.setSpan(ForegroundColorSpan(Color.BLUE),lastname.text.length, lastnameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    start_nrSpan.setSpan(ForegroundColorSpan(Color.BLUE), start_nr.text.length, start_nrSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    pseudoSpan.setSpan(ForegroundColorSpan(Color.BLUE), pseudo.text.length, pseudoSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    name.text = nameSpan
                    lastname.text = lastnameSpan
                    start_nr.text = start_nrSpan
                    pseudo.text = pseudoSpan

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    dialog?.window?.setLayout(1000,900)
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
                    var rootView : View = inflater.inflate(R.layout.dialog_edit_user, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    rootView.editButton.setOnClickListener {
                        //chcemy do bazy danych i do tabeli wklepać wartości z edycji

                        //nowe wartości
                        var editedName = edit_imie.text.toString()
                        var editedLastName = nazwisko_edit.text.toString()
                        var editedStartNr = nr_startowy_edit.text.toString()
                        var editedPsudonim = pseudonim_edit.text.toString()

                        var rowToEdit = 0;
                        rowToEdit = clickedData!!.get(0).toInt()



                        if (editedName.isNullOrBlank() || editedLastName.isNullOrBlank() ||
                            editedPsudonim.isNullOrBlank() || editedStartNr.isNullOrBlank()
                        ) {
                            Toast.makeText(this@ShowAndEditParticipant, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                        } else {
                            var isCorrect = true


                            if (!NameValidator.validate(editedName)) {
                                Toast.makeText(
                                    this@ShowAndEditParticipant,
                                    "Nieprawidłowy format imienia!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            } else if (!LastNameValidator.validate(editedLastName)) {
                                Toast.makeText(
                                    this@ShowAndEditParticipant,
                                    "Nieprawidłowy format nazwiska!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }

                            else if (checkIfPresentInDB(editedStartNr, "nr_startowy", rowToEdit ))
                            {
                                Toast.makeText(
                                    this@ShowAndEditParticipant,
                                    "Osoba o podanym nr startowym już istnieje w bazie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }
                            else if (checkIfPresentInDB(editedPsudonim, "pseudonim", rowToEdit))
                            {
                                Toast.makeText(
                                    this@ShowAndEditParticipant,
                                    "Osoba o podanym pseudonimie już istnieje w bazie!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }

                            //tu jeszcze powinienem sprawidzc czy osoba o tym numerze starowym już mnie jest w bazie

                            //Po aktualizacji wracam do ekranu głównego administratora.
                            if (isCorrect) {


                                connector.prepareQuery("UPDATE uczestnicy SET imie = ?, nazwisko = ?, pseudonim = ? " +
                                        ", nr_startowy = ? WHERE id_konta = ?;")
                                connector.setStrVar(editedName, 1)
                                connector.setStrVar(editedLastName, 2)
                                connector.setStrVar(editedPsudonim, 3)
                                connector.setStrVar(editedStartNr, 4)
                                connector.setIntVar(rowToEdit, 5)



                                try{connector.executeQuery()} catch (e: Exception){print("erorr")}
                                connector.closeQuery()

                                //dane w bazie zmienione




                                adapterData = SimpleTableDataAdapter(this@ShowAndEditParticipant, initData())
                                adapterData.setTextSize(12)
                                adapterData.setTextColor(Color.BLACK)
                                tableView.dataAdapter = adapterData
                                init_volunteers()
                                customSpinner.text = ""
                                dismiss()

                            }
                            }

                    }


                    val name = rootView.findViewById<EditText>(R.id.edit_imie)
                    val lastname = rootView.findViewById<EditText>(R.id.nazwisko_edit)
                    val start_nr = rootView.findViewById<EditText>(R.id.nr_startowy_edit)
                    val pseudo = rootView.findViewById<EditText>(R.id.pseudonim_edit)

                    name.setText(clickedData?.get(2))
                    lastname.setText(clickedData?.get(3))
                    start_nr.setText(clickedData?.get(1))
                    pseudo.setText(clickedData?.get(4))

                    return rootView
                }

                override fun onStart() {
                    super.onStart()
                    getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corners);
                    dialog?.window?.setLayout(1000,1020)
                }
            }

            val dialog  = MyCustomEditDialog()
            dialog.show(supportFragmentManager, "CustomEditDialog")
        }
        }
    }





