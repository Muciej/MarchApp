package com.dreamteam.marchapp.logic.shared


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.R.id.*
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.dataclasses.Participant
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
import com.dreamteam.marchapp.databinding.ActivityOrganiserShowVolunteersBinding

class ShowAndEditParticipant : AppCompatActivity(), TableDataClickListener<Array<String>>{
    private var lastClickedRow = -1
    var data : MutableList<Array<String>> = mutableListOf()
    lateinit var tableView : TableView<Array<String>>
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var participantList :MutableList<Participant>
    private var participantNames = Vector<String>()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: ActivityOrganiserShowVolunteersBinding
    private lateinit var dataAdapter : SimpleTableDataAdapter

    //lista uczestników do pokazania w tabeli
    private lateinit var participantsToShow : MutableList<Array<String>>
    var accessLevel = ""

    private fun participantUpdated(newParticipants: ArrayList<Participant>) {
        participantList = newParticipants
        participantNames.clear()
        participantNames.add("wszyscy")
        participantNames.addAll(newParticipants.map { "${it.name} ${it.surname}" })
        participantNames.add("nie wybrano")

        init_spinner_and_table()
    }

    private fun init_spinner_and_table() {
        adapter = ArrayAdapter(this@ShowAndEditParticipant, android.R.layout.simple_list_item_1, participantNames)
        participantsToShow = initData()

        dataAdapter.data.clear()
        dataAdapter.addAll(participantsToShow)
        dataAdapter.notifyDataSetChanged()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganiserShowVolunteersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.allParticipants.observe(this) { newParticipants ->
            participantUpdated(newParticipants)
        }

        binding.customSpinner.text = "Wybierz uczestnika"
        binding.ChooseVolonteersTextView.hint= "Wybierz uczestnika"

        //inicjalizacja tabeli
        setTable()

        accessLevel = intent.getStringExtra("accessLevel")!!
        if (accessLevel == "Organiser" || accessLevel == "Volunteer")
        {
            binding.editCheckbox.isClickable=false
            binding.editCheckbox.visibility=View.INVISIBLE
        }

        binding.btnBack.setOnClickListener{ goBack() }
        binding.customSpinner.setOnClickListener{ show_dialog() }
    }

    private fun goBack() {
        lateinit var intent : Intent
        when (accessLevel) {
            "Organiser" -> intent = Intent(this, OrganisatorMain::class.java)
            "Admin" -> intent = Intent(this, AdministratorMain::class.java)
            "Volunteer" -> intent = Intent(this, VolunteerMain::class.java)
        }
        startActivity(intent)
    }

    private fun setTable() {
        tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        tableView.addDataClickListener(this)
        tableView.setHeaderBackgroundColor(Color.rgb(		98, 0, 238))

        //kolory wierszy na przemian
        val colorEvenRows = Color.rgb(224,224,224)
        val colorOddRows = Color.WHITE
        tableView.setDataRowBackgroundProvider(
            TableDataRowBackgroundProviders.alternatingRowColors(
                colorEvenRows,
                colorOddRows
            )
        )

        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditParticipant, "Id","Nr", "Imie", "Nazwisko", "Pseudonim")
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)

        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 100)
        columnModel.setColumnWidth(2, 250)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel


        participantsToShow = arrayListOf()
        dataAdapter = SimpleTableDataAdapter(this@ShowAndEditParticipant, participantsToShow)

        dataAdapter.setTextSize(12)
        dataAdapter.setTextColor(Color.BLACK)
        tableView.dataAdapter = dataAdapter
    }


    private fun show_dialog() {
        val dialog = Dialog(this@ShowAndEditParticipant)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.show()

        val edittext = dialog.findViewById<EditText>(edit_text)
        val listview = dialog.findViewById<ListView>(listView)

        listview.adapter = adapter

        edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(charSequence)
            }
            override fun afterTextChanged(p0: Editable?) {}})


        listview.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, clicked, _ ->
                binding.customSpinner.text = adapter.getItem(clicked)

                if (listview.getItemAtPosition(clicked).toString().lowercase() == "wszyscy") participantsToShow = initData()
                else if (listview.getItemAtPosition(clicked).toString().lowercase()!="nie wybrano") participantsToShow = selectData(listview.getItemAtPosition(clicked).toString())
                else participantsToShow.clear()

                dataAdapter.data.clear()
                dataAdapter.addAll(participantsToShow)
                dataAdapter.notifyDataSetChanged()

                dialog.dismiss()
            }

    }

    private fun selectData(name: String):MutableList<Array<String>>
    {
        val tmp = name.split(" ")
        val participant:Participant = participantList!!.filter { it.name == tmp[0] && it.surname == tmp[1] }.first()

        data.clear()
        data.add(arrayOf(
            participant.accId.toString(),
            participant.startNumber.toString(),
            participant.name,
            participant.surname,
            participant.nickname))

        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        data.clear()
        participantList.forEach { data.add(arrayOf(
            it.accId.toString(),
            it.startNumber.toString(),
            it.name,
            it.surname,
            it.nickname)) }
        return data
    }

    private fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        /*
        return participantList.any { participant ->
            val fieldValue = participant::class.members
                .filterIsInstance<KProperty1<Participant, Any?>>()
                .find { it.name == field }
                ?.get(participant)
                ?.toString()

            fieldValue == obj}

         */

        if (field == "nr_startowy") return participantList.any { participant -> participant.startNumber == obj.toInt() && participant.accId != id}
        else if (field == "pseudonim") return participantList.any { participant -> participant.nickname == obj && participant.accId != id}
        return false
    }

    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {

        lastClickedRow = rowIndex

        if (!binding.editCheckbox.isChecked)
        {
            class MyCustomDialog: DialogFragment() {
                override fun onCreateView(
                    inflater: LayoutInflater,
                    container: ViewGroup?,
                    savedInstanceState: Bundle?
                ): View {

                    val rootView : View = inflater.inflate(R.layout.dialog_zoom_data_user, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val name = rootView.findViewById<TextView>(R.id.name)
                    val lastname = rootView.findViewById<TextView>(R.id.lastname)
                    val start_nr = rootView.findViewById<TextView>(R.id.start_no)
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
                    dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
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
                ): View {
                    val rootView : View = inflater.inflate(R.layout.dialog_edit_user, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val name = rootView.findViewById<EditText>(R.id.edit_name)
                    val lastname = rootView.findViewById<EditText>(R.id.lastname_edit)
                    val start_nr = rootView.findViewById<EditText>(R.id.start_no_edit)
                    val pseudo = rootView.findViewById<EditText>(R.id.pseudonim_edit)

                    rootView.editButton.setOnClickListener {

                        //nowe wartości
                        val editedName = edit_name.text.toString()
                        val editedLastName = lastname_edit.text.toString()
                        val editedStartNr = start_no_edit.text.toString()
                        val editedPseudonim = pseudonim_edit.text.toString()

                        val editedParticipantId: Int = clickedData!![0].toInt()

                        val qrCode = participantList.find { participant ->
                            participant.accId == clickedData[0].toInt()
                        }?.qrCodeData

                        val isCorrect = validateEditedData(editedName, editedLastName, editedStartNr, editedPseudonim, editedParticipantId)

                        if (isCorrect) {
                            val editedParticipant = Participant(editedParticipantId, editedStartNr.toInt(), editedName, editedLastName, editedPseudonim, qrCode!!)
                            dataViewModel.updateParticipant(editedParticipant)
                            binding.customSpinner.text = ""
                            dismiss()
                        }
                    }

                    name.setText(clickedData?.get(2))
                    lastname.setText(clickedData?.get(3))
                    start_nr.setText(clickedData?.get(1))
                    pseudo.setText(clickedData?.get(4))

                    return rootView
                }

                private fun validateEditedData(
                    editedName: String,
                    editedLastName: String,
                    editedStartNr: String,
                    editedPseudonim: String,
                    editedParticipantId: Int
                ): Boolean {
                    if (editedName.isBlank() || editedLastName.isBlank() ||
                        editedPseudonim.isBlank() || editedStartNr.isBlank()
                    ) {
                        Toast.makeText(this@ShowAndEditParticipant, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    if (!NameValidator.validate(editedName)) {
                        showInfo("Nieprawidłowy format imienia!")
                        return false
                    }

                    if (!LastNameValidator.validate(editedLastName)) {
                        showInfo("Nieprawidłowy format nazwiska!")
                        return false
                    }

                    if (checkIfPresentInDB(editedStartNr, "nr_startowy", editedParticipantId )) {
                        showInfo("Osoba o podanym nr startowym już istnieje w bazie!")
                        return false
                    }

                    if (checkIfPresentInDB(editedPseudonim, "pseudonim", editedParticipantId)) {
                        showInfo("Osoba o podanym pseudonimie już istnieje w bazie!")
                        return false
                    }

                    return true
                }


                private fun showInfo(mess: String) {
                    Toast.makeText(this@ShowAndEditParticipant, mess, Toast.LENGTH_SHORT).show()
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





