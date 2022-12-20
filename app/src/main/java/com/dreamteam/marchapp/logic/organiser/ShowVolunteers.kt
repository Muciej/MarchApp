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
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_edit_volunteers.*
import kotlinx.android.synthetic.main.dialog_edit_volunteers.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb


//doodać oba scrolle
class ShowVolunteers : AppCompatActivity(), TableDataClickListener<Array<String>> {
    var lastClickedRow = -1
    lateinit var data : Array<Array<String>>
    lateinit var adapterData: SimpleTableDataAdapter
    lateinit var tableView : TableView<Array<String>>
    lateinit var editModeCheckbox : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organiser_show_volunteers)
        var accessLevel = intent.getStringExtra("accessLevel")
        val backBtn = findViewById<Button>(btnBack)
        tableView = findViewById<View>(volunteersTable) as TableView<Array<String>>
        val customSpinner = findViewById<TextView>(textView)
        val adapterHead = SimpleTableHeaderAdapter(this@ShowVolunteers, "Id", "Imie", "Nazwisko", "telefon", "Email")
        tableView.addDataClickListener(this)
        tableView.setHeaderBackgroundColor(Color.rgb(		98, 0, 238))
        val colorEvenRows = Color.rgb(224,224,224)
        val colorOddRows = Color.WHITE
        tableView.setDataRowBackgroundProvider(
            TableDataRowBackgroundProviders.alternatingRowColors(
                colorEvenRows,
                colorOddRows
            )
        )
        editModeCheckbox = findViewById<CheckBox>(editCheckbox)
        if (accessLevel == "Organiser")
        {
            editModeCheckbox.isClickable=false
            editModeCheckbox.visibility=View.INVISIBLE
        }


        //tu będzie leciało zapytanie do bazy zby pobrać dane wolontariuszy
        val volunteersList = arrayOf("Nie wybrano", "Nowak Tomasz", "Jakiś Andrzej", "Kowalska Anna", "Wszyscy")
        data = arrayOf(arrayOf("1","Tomasz", "Nowak", "123456789", "tomhol@wp.pl"),
            arrayOf("2","Andrzej", "Jakiś", "123456789", "andjak@wp.pl"),
            arrayOf("3","Aneta", "Kura", "123456789", "anetaKura@wp.pl"),
            arrayOf("4","Jadwiga", "Czarek", "123456789", "czarek@wp.pl"),
            arrayOf("5","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("6","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("7","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("8","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("9","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("10","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("11","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("12","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("13","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("14","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("15","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("16","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("17","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("18","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("19","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("20","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("21","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("22","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("23","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("24","Anna", "Kowalska", "123456789", "annkow@wp.pl"),
            arrayOf("25","Anna", "Kowalska", "123456789", "annkow@wp.pl"))

        backBtn.setOnClickListener{
            lateinit var intent : Intent
            if (accessLevel == "Organiser") intent = Intent(this, OrganisatorMain::class.java)
            else if (accessLevel == "Admin") intent = Intent(this, AdministratorMain::class.java)
            startActivity(intent)
        }

        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
        tableView.dataAdapter = SimpleTableDataAdapter(this, arrayOf())



        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 200)
        columnModel.setColumnWidth(2, 250)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel

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

    //po dodaniu id można to zrobić lepiej
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
                    var rootView : View = inflater.inflate(R.layout.dialog_zoom_data, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    val name = rootView.findViewById<TextView>(R.id.imie)
                    val lastname = rootView.findViewById<TextView>(R.id.nazwisko)
                    val phone = rootView.findViewById<TextView>(R.id.tel)
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
                    var rootView : View = inflater.inflate(R.layout.dialog_edit_volunteers, container, false)
                    rootView.backb.setOnClickListener { dismiss() }

                    rootView.editButton.setOnClickListener {
                        //chcemy do bazy danych i do tabeli wklepać wartości z edycji

                        //nowe wartości
                        var editedName = edit_imie.text.toString()
                        var editedLastName = nazwisko_edit.text.toString()
                        var editedPhone = nr_telefonu_edit.text.toString()
                        var editedMail = mail_edit.text.toString()



                        if (editedName.isNullOrBlank() || editedLastName.isNullOrBlank() ||
                            editedMail.isNullOrBlank() || editedPhone.isNullOrBlank()
                        ) {
                            Toast.makeText(this@ShowVolunteers, "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                        } else {
                            var isCorrect = true

                            //Tu lecą zapytania do bazy
                            //1. Zapytanie o to czy dana nazwa użytkownika istnieje w bazie,
                            //2. Ewentualne zapytania o format email i numeru telefonu
                            //(zależy od tego jak będziemy to sprawdzać).

                            if (!EmailValidator.validate(editedMail)) {
                                Toast.makeText(
                                    this@ShowVolunteers,
                                    "Nieprawidłowy format email!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            } else if (!PhoneValidator.validate(editedPhone)) {
                                Toast.makeText(
                                    this@ShowVolunteers,
                                    "Nieprawidłowy format numeru!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isCorrect = false
                            }

                            //Po aktualizacji wracam do ekranu głównego administratora.
                            if (isCorrect) {
                                //pobiera
                                var rowToEdit = clickedData?.get(0)

                                //zamiast tego bedzie zapytanie do bazy
                                for (row in data)
                                {
                                    if (row[0] == rowToEdit)
                                    {
                                        row[1] = editedName
                                        row[2] = editedLastName
                                        row[3] = editedPhone
                                        row[4] = editedMail
                                        break
                                    }
                                }

                                adapterData = SimpleTableDataAdapter(this@ShowVolunteers, data)
                                adapterData.setTextSize(12)
                                adapterData.setTextColor(Color.BLACK)
                                tableView.dataAdapter = adapterData
                                dismiss()
                            }
                            }

                    }


                    val name = rootView.findViewById<EditText>(R.id.edit_imie)
                    val lastname = rootView.findViewById<EditText>(R.id.nazwisko_edit)
                    val phone = rootView.findViewById<EditText>(R.id.nr_telefonu_edit)
                    val mail = rootView.findViewById<EditText>(R.id.mail_edit)

                    name.setText(clickedData?.get(1))
                    lastname.setText(clickedData?.get(2))
                    phone.setText(clickedData?.get(3))
                    mail.setText(clickedData?.get(4))

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





