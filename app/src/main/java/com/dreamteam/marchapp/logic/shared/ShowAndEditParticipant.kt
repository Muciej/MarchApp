package com.dreamteam.marchapp.logic.shared


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
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.dataclasses.Participant
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.dialog_edit_user.*
import kotlinx.android.synthetic.main.dialog_edit_user.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.backb

class ShowAndEditParticipant : ShowAndEditObject() {
    private lateinit var participantList: ArrayList<Participant>

    private fun participantUpdated(newPeople: ArrayList<Participant>) {
        participantList = newPeople
        peopleNames.clear()
        peopleNames.add("wszyscy")
        peopleNames.addAll(newPeople.map { "${it.name} ${it.surname}" })
        peopleNames.add("nie wybrano")

        init_spinner_and_table()
    }

    override fun setDataViewModel() {
        super.setDataViewModel()
        dataViewModel.allParticipants.observe(this) { newPeople ->
            participantUpdated(newPeople)
        }
    }

    override fun setAccesssLevel() {
        accessLevel = intent.getStringExtra("accessLevel")!!
        if (accessLevel == "Organiser" || accessLevel == "Volunteer")
        {
            binding.editCheckbox.isClickable=false
            binding.editCheckbox.visibility= View.INVISIBLE
        }

        binding.customSpinner.text = "Wybierz uczestnika"
        binding.ChooseVolonteersTextView.text= "Wybierz uczestnika"
    }


    override fun setHeader() {
        val adapterHead = SimpleTableHeaderAdapter(this@ShowAndEditParticipant, "Id","Nr", "Imie", "Nazwisko", "Pseudonim")
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
    }


    override fun selectData(name: String):MutableList<Array<String>>
    {
        val tmp = name.split(" ")
        val participant:Participant =
            participantList.first { it.name == tmp[0] && it.surname == tmp[1] }

        data.clear()
        data.add(arrayOf(
            participant.accId.toString(),
            participant.startNumber.toString(),
            participant.name,
            participant.surname,
            participant.nickname))

        return data
    }

    override fun initData():MutableList<Array<String>>
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

    override fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean {
        if (field == "nr_startowy") return participantList.any { participant -> participant.startNumber == obj.toInt() && participant.accId != id}
        else if (field == "pseudonim") return participantList.any { participant -> participant.nickname == obj && participant.accId != id}
        return false
    }

    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {

        class MyCustomParticipantDialog: BaseDialogFragment() {
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
        }

        class MyCustomParticipantEditDialog: BaseDialogFragment() {

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

                    val isCorrect = validateEditedData(editedName, editedLastName, editedStartNr, editedPseudonim, editedParticipantId.toString())

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

            override fun validateEditedData(vararg values: String): Boolean {
                super.validateEditedData(*values)

                if (checkIfPresentInDB(values[2], "nr_startowy", values[4].toInt())) {
                    showInfo("Osoba o podanym nr startowym już istnieje w bazie!")
                    return false
                }

                if (checkIfPresentInDB(values[3], "pseudonim", values[4].toInt())) {
                    showInfo("Osoba o podanym pseudonimie już istnieje w bazie!")
                    return false
                }

                return true
            }
        }


        val dialogFragment =
            if (binding.editCheckbox.isChecked)
            {
                MyCustomParticipantEditDialog()
            } else {
                MyCustomParticipantDialog()
            }


        dialogFragment.arguments = Bundle().apply {
            putStringArray("clickedData", clickedData)
        }

        dialogFragment.show(supportFragmentManager, if (binding.editCheckbox.isChecked) "CustomEditDialog" else "CustomDialog")
    }


}





