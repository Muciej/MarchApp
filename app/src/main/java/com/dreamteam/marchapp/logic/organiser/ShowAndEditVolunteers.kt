package com.dreamteam.marchapp.logic.organiser


import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.dataclasses.Volounteer
import com.dreamteam.marchapp.logic.validation.EmailValidator
import com.dreamteam.marchapp.logic.validation.PhoneValidator
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.dialog_edit_volunteers.*
import kotlinx.android.synthetic.main.dialog_edit_volunteers.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb
import com.dreamteam.marchapp.logic.shared.ShowAndEditPerson


class ShowAndEditVolunteers : ShowAndEditPerson() {
    private lateinit var volunteersList: ArrayList<Volounteer>

    private fun volunteerUpdated(newVolunteers: ArrayList<Volounteer>) {
        volunteersList = newVolunteers
        peopleNames.clear()
        peopleNames.add("wszyscy")
        peopleNames.addAll(newVolunteers.map { "${it.name} ${it.surname}" })
        peopleNames.add("nie wybrano")

        init_spinner_and_table()
    }

    override fun setDataViewModel() {
        super.setDataViewModel()
        dataViewModel.allVolounteers.observe(this) { newPeople ->
            volunteerUpdated(newPeople)
        }
    }


    override fun setAccesssLevel() {
        accessLevel = intent.getStringExtra("accessLevel")!!

        if (accessLevel == "Organiser") {
            binding.editCheckbox.isClickable = false
            binding.editCheckbox.visibility = View.INVISIBLE
        }
    }


    override fun setHeader() {
        val adapterHead = SimpleTableHeaderAdapter(
            this@ShowAndEditVolunteers,
            "Id",
            "Imie",
            "Nazwisko",
            "telefon",
            "Email"
        )
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
    }


    override fun selectData(name: String): MutableList<Array<String>> {
        val tmp = name.split(" ")
        val volounteer: Volounteer =
            volunteersList.first { it.name == tmp[0] && it.surname == tmp[1] }

        data.clear()

        data.add(
            arrayOf(
                volounteer.accountId.toString(),
                volounteer.name,
                volounteer.surname,
                volounteer.phoneNumber,
                volounteer.mail
            )
        )
        return data
    }


    override fun initData(): MutableList<Array<String>> {
        data.clear()
        volunteersList.forEach {
            data.add(
                arrayOf(
                    it.accountId.toString(),
                    it.name,
                    it.surname,
                    it.phoneNumber,
                    it.mail
                )
            )
        }
        return data
    }


    override fun checkIfPresentInDB(obj: String, field: String, id: Int): Boolean {
        if (field == "nr_telefonu") return volunteersList.any { volunteer -> volunteer.phoneNumber == obj && volunteer.accountId != id }
        else if (field == "mail") return volunteersList.any { volunteer -> volunteer.mail == obj && volunteer.accountId != id }
        return false
    }



    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {
        class MyCustomVolunteerEditDialog : BaseDialogFragment() {
            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {


                val rootView : View = inflater.inflate(R.layout.dialog_edit_volunteers, container, false)
                rootView.backb.setOnClickListener { dismiss() }

                val name = rootView.findViewById<EditText>(R.id.edit_name)
                val lastname = rootView.findViewById<EditText>(R.id.lastname_edit)
                val phone = rootView.findViewById<EditText>(R.id.phone_no_edit)
                val mail = rootView.findViewById<EditText>(R.id.mail_edit)

                name.setText(clickedData?.get(1))
                lastname.setText(clickedData?.get(2))
                phone.setText(clickedData?.get(3))
                mail.setText(clickedData?.get(4))

                rootView.editButton.setOnClickListener {

                    val editedVolunteerId: Int = clickedData!![0].toInt()

                    //wartości nie do edycji
                    val staffId = volunteersList.find { volunteer ->
                        volunteer.accountId == clickedData[0].toInt()
                    }?.staffId

                    val login = volunteersList.find { volunteer ->
                        volunteer.accountId == clickedData[0].toInt()
                    }?.login

                    val editedName = edit_name.text.toString()
                    val editedLastName = lastname_edit.text.toString()
                    val editedPhone = phone_no_edit.text.toString()
                    val editedMail = mail_edit.text.toString()


                    val isCorrect = validateEditedData(
                        editedName,
                        editedLastName,
                        editedPhone,
                        editedMail,
                        editedVolunteerId.toString())

                    if (isCorrect) {
                        val editedVolunteer = Volounteer(editedVolunteerId, staffId!!.toInt(), editedName, editedLastName, editedPhone, editedMail, login!!)
                        dataViewModel.updateVolunteer(editedVolunteer)
                        binding.customSpinner.text = ""
                        dismiss()
                    }
                }

                return rootView
            }

            override fun validateEditedData(vararg values: String): Boolean {
                super.validateEditedData(*values)

                if (!EmailValidator.validate(values[3])) {
                    showInfo("Nieprawidłowy format maila!")
                    return false
                }

                if (!PhoneValidator.validate(values[2])) {
                    showInfo("Nieprawidłowy format nr telefonu!")
                    return false
                }

                if (checkIfPresentInDB(values[2], "nr_telefonu", values[4].toInt() )) {
                    showInfo("Osoba o podanym nr telefonu już istnieje w bazie!")
                    return false
                }

                if (checkIfPresentInDB(values[3], "mail", values[4].toInt())) {
                    showInfo("Osoba o podanym mailu już istnieje w bazie!")
                    return false
                }

                return true
            }
        }

        class MyCustomVolunteerDialog : BaseDialogFragment() {
            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {

                val rootView = inflater.inflate(R.layout.dialog_zoom_data, container, false)
                rootView.findViewById<Button>(R.id.backb).setOnClickListener { dismiss() }

                val phone = rootView.findViewById<TextView>(com.dreamteam.marchapp.R.id.phone)
                val mail = rootView.findViewById<TextView>(com.dreamteam.marchapp.R.id.mail)
                val name = rootView.findViewById<TextView>(com.dreamteam.marchapp.R.id.name)
                val lastname = rootView.findViewById<TextView>(com.dreamteam.marchapp.R.id.lastname)

                val phoneSpan = SpannableString(phone?.text.toString() + (clickedData?.get(3)))
                val mailSpan = SpannableString(mail?.text.toString() + (clickedData?.get(4)))
                val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(1)))
                val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(2)))

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
        }


        val dialogFragment =
        if (binding.editCheckbox.isChecked)
        {
            MyCustomVolunteerEditDialog()
        } else {
            MyCustomVolunteerDialog()
        }


        dialogFragment.arguments = Bundle().apply {
            putStringArray("clickedData", clickedData)
        }

        dialogFragment.show(supportFragmentManager, if (binding.editCheckbox.isChecked) "CustomEditDialog" else "CustomDialog")
    }
}






