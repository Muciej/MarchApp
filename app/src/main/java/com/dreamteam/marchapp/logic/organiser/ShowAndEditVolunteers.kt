package com.dreamteam.marchapp.logic.organiser


import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.R.id.*
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.database.dataclasses.Volounteer
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.NameValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_edit_volunteers.*
import kotlinx.android.synthetic.main.dialog_edit_volunteers.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb
import java.util.Vector
import com.dreamteam.marchapp.databinding.ActivityOrganiserShowVolunteersBinding


class ShowAndEditVolunteers : AppCompatActivity(), TableDataClickListener<Array<String>> {
    private var lastClickedRow = -1
    var data : MutableList<Array<String>> = mutableListOf()
    private lateinit var tableView : TableView<Array<String>>
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var volunteersList :MutableList<Volounteer>
    private var volunteersNames = Vector<String>()
    private lateinit var dataViewModel: DataViewModel
    private lateinit var binding: ActivityOrganiserShowVolunteersBinding
    private lateinit var dataAdapter : SimpleTableDataAdapter

    private lateinit var volunteersToShow : MutableList<Array<String>>
    private var accessLevel = ""



    private fun volunteerUpdated(newVolunteers: ArrayList<Volounteer>) {
        volunteersList = newVolunteers
        volunteersNames.clear()
        volunteersNames.add("wszyscy")
        volunteersNames.addAll(newVolunteers.map { "${it.name} ${it.surname}" })
        volunteersNames.add("nie wybrano")

        init_spinner_and_table()
    }

    private fun init_spinner_and_table() {
        adapter = ArrayAdapter(this@ShowAndEditVolunteers, android.R.layout.simple_list_item_1, volunteersNames)
        volunteersToShow = initData()

        dataAdapter.data.clear()
        dataAdapter.addAll(volunteersToShow)
        dataAdapter.notifyDataSetChanged()
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganiserShowVolunteersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.allVolounteers.observe(this) { newVolunteers ->
            volunteerUpdated(newVolunteers)
        }

        //inicjalizacja tabeli
        setTable()

        accessLevel = intent.getStringExtra("accessLevel")!!

        if (accessLevel == "Organiser")
        {
            binding.editCheckbox.isClickable=false
            binding.editCheckbox.visibility=View.INVISIBLE
        }


        binding.btnBack.setOnClickListener{
            goBack()
        }

        binding.customSpinner.setOnClickListener{
            show_dialog() }
    }

    private fun show_dialog() {
        val dialog = Dialog(this@ShowAndEditVolunteers)
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

                if (listview.getItemAtPosition(clicked).toString().lowercase() == "wszyscy") volunteersToShow = initData()
                else if (listview.getItemAtPosition(clicked).toString().lowercase()!="nie wybrano") volunteersToShow = selectData(listview.getItemAtPosition(clicked).toString())
                else volunteersToShow.clear()

                dataAdapter.data.clear()
                dataAdapter.addAll(volunteersToShow)
                dataAdapter.notifyDataSetChanged()

                dialog.dismiss()
            }
    }

    private fun goBack() {
        lateinit var intent : Intent
        if (accessLevel == "Organiser") intent = Intent(this, OrganisatorMain::class.java)
        else if (accessLevel == "Admin") intent = Intent(this, AdministratorMain::class.java)
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

        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditVolunteers, "Id", "Imie", "Nazwisko", "telefon", "Email")

        tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        tableView.addDataClickListener(this)
        tableView.setHeaderBackgroundColor(Color.rgb(		98, 0, 238))

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)

        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 200)
        columnModel.setColumnWidth(2, 250)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel


        volunteersToShow = arrayListOf()
        dataAdapter = SimpleTableDataAdapter(this@ShowAndEditVolunteers, volunteersToShow)

        dataAdapter.setTextSize(12)
        dataAdapter.setTextColor(Color.BLACK)
        tableView.dataAdapter = dataAdapter
    }
    private fun selectData(name: String):MutableList<Array<String>>
    {
        val tmp = name.split(" ")
        val volounteer: Volounteer =
            volunteersList.first { it.name == tmp[0] && it.surname == tmp[1] }

        data.clear()

        data.add(arrayOf(
            volounteer.accountId.toString(),
            volounteer.name,
            volounteer.surname,
            volounteer.phoneNumber,
            volounteer.mail)
        )
        return data
    }

    private fun initData():MutableList<Array<String>>
    {
        data.clear()
        volunteersList.forEach { data.add(arrayOf(
            it.accountId.toString(),
            it.name,
            it.surname,
            it.phoneNumber,
            it.mail)
        ) }
        return data
    }

    private fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        if (field == "nr_telefonu") return volunteersList.any { volunteer -> volunteer.phoneNumber == obj && volunteer.accountId != id}
        else if (field == "mail") return volunteersList.any { volunteer -> volunteer.mail == obj && volunteer.accountId != id}
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

                    dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners)
                    val rootView : View = inflater.inflate(R.layout.dialog_zoom_data, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val name = rootView.findViewById<TextView>(R.id.name)
                    val lastname = rootView.findViewById<TextView>(R.id.lastname)
                    val phone = rootView.findViewById<TextView>(R.id.phone)
                    val mail = rootView.findViewById<TextView>(R.id.mail)

                    val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(1)))
                    val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(2)))
                    val phoneSpan = SpannableString(phone?.text.toString() + (clickedData?.get(3)))
                    val mailSpan = SpannableString(mail?.text.toString() + (clickedData?.get(4)))

                    nameSpan.setSpan(ForegroundColorSpan(Color.BLUE), name.text.length, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    lastnameSpan.setSpan(ForegroundColorSpan(Color.BLUE),lastname.text.length, lastnameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    phoneSpan.setSpan(ForegroundColorSpan(Color.BLUE), phone.text.length, phoneSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    mailSpan.setSpan(ForegroundColorSpan(Color.BLUE), mail.text.length, mailSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    name.text = nameSpan
                    lastname.text = lastnameSpan
                    phone.text = phoneSpan
                    mail.text = mailSpan

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

                    dialog?.window?.setLayout((width*0.95).toInt(), (height * 0.5).toInt())
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


                    dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val rootView : View = inflater.inflate(R.layout.dialog_edit_volunteers, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    rootView.editButton.setOnClickListener {
                        //chcemy do bazy danych i do tabeli wklepać wartości z edycji

                        //nowe wartości
                        val editedName = edit_name.text.toString()
                        val editedLastName = lastname_edit.text.toString()
                        val editedPhone = phone_no_edit.text.toString()
                        val editedMail = mail_edit.text.toString()

                        val editedVolunteerId: Int = clickedData!![0].toInt()
                        
                        //wartości nie do edycji
                        val staffId = volunteersList.find { volunteer ->
                            volunteer.accountId == clickedData[0].toInt()
                        }?.staffId

                        val login = volunteersList.find { volunteer ->
                            volunteer.accountId == clickedData[0].toInt()
                        }?.login


                        val isCorrect = validateEditedData(editedName, editedLastName, editedPhone, editedMail, editedVolunteerId)

                        if (isCorrect) {
                            val editedVolunteer = Volounteer(editedVolunteerId, staffId!!.toInt(), editedName, editedLastName, editedPhone, editedMail, login!!)
                            dataViewModel.updateVolunteer(editedVolunteer)
                            binding.customSpinner.text = ""
                            dismiss()
                        }
                    }


                    val name = rootView.findViewById<EditText>(R.id.edit_name)
                    val lastname = rootView.findViewById<EditText>(R.id.lastname_edit)
                    val phone = rootView.findViewById<EditText>(R.id.phone_no_edit)
                    val mail = rootView.findViewById<EditText>(R.id.mail_edit)

                    name.setText(clickedData?.get(1))
                    lastname.setText(clickedData?.get(2))
                    phone.setText(clickedData?.get(3))
                    mail.setText(clickedData?.get(4))

                    return rootView
                }

                private fun validateEditedData(
                    editedName: String,
                    editedLastName: String,
                    editedPhone: String,
                    editedMail: String,
                    editedVolunteerId: Int
                ): Boolean {
                    if (editedName.isBlank() || editedLastName.isBlank() ||
                        editedMail.isBlank() || editedPhone.isBlank()
                    ) {
                        Toast.makeText(this@ShowAndEditVolunteers, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
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

                    if (!EmailValidator.validate(editedMail)) {
                        showInfo("Nieprawidłowy format maila!")
                        return false
                    }

                    if (!PhoneValidator.validate(editedPhone)) {
                        showInfo("Nieprawidłowy format nr telefonu!")
                        return false
                    }

                    if (checkIfPresentInDB(editedPhone, "nr_telefonu", editedVolunteerId )) {
                        showInfo("Osoba o podanym nr telefonu już istnieje w bazie!")
                        return false
                    }

                    if (checkIfPresentInDB(editedMail, "mail", editedVolunteerId)) {
                        showInfo("Osoba o podanym mailu już istnieje w bazie!")
                        return false
                    }

                    return true

                }

                private fun showInfo(mess: String) {
                    Toast.makeText(this@ShowAndEditVolunteers, mess, Toast.LENGTH_SHORT).show()
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

            val dialog  = MyCustomEditDialog()
            dialog.show(supportFragmentManager, "CustomEditDialog")
        }
        }
    }





