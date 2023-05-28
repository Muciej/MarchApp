package com.dreamteam.marchapp.logic.admin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.fragment.app.DialogFragment
import com.dreamteam.marchapp.R
import com.dreamteam.marchapp.database.dataclasses.CheckPoint
import com.dreamteam.marchapp.database.dataclasses.Volounteer
import com.dreamteam.marchapp.logic.shared.ShowAndEditObject
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.dialog_change_point.view.*


class AssignVolunteerToPoint : ShowAndEditObject()
{
    var points  = arrayListOf<String>()
    private lateinit var volunteersList: ArrayList<Volounteer>


    private fun volunteerUpdated(newVolunteers: ArrayList<Volounteer>) {
        volunteersList = newVolunteers
        peopleNames.clear()
        peopleNames.add("wszyscy")
        peopleNames.addAll(newVolunteers.map { "${it.name} ${it.surname}" })
        peopleNames.add("nie wybrano")

        init_spinner_and_table()
    }

    private fun pointsUpdated(newPoints: ArrayList<CheckPoint>) {
        points.addAll(newPoints.map { "${it.id}" })
    }

    override fun setDataViewModel() {
        super.setDataViewModel()
        dataViewModel.allVolounteers.observe(this) { newPeople ->
            volunteerUpdated(newPeople)
        }

        dataViewModel.checkPoints.observe(this) { newPoints ->
            pointsUpdated(newPoints)
        }
    }


    override fun setHeader() {
        val adapterHead = SimpleTableHeaderAdapter(
            this@AssignVolunteerToPoint,
            "Id",
            "Imie",
            "Nazwisko",
            "Punkt"
        )
        tableView.headerAdapter = adapterHead
        adapterHead.setTextSize(15)
        adapterHead.setTextColor(Color.WHITE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val columnModel = TableColumnPxWidthModel(4, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 300)
        columnModel.setColumnWidth(2, 400)
        columnModel.setColumnWidth(3, 250)
        tableView.columnModel = columnModel

        binding.editCheckbox.isClickable = false
        binding.editCheckbox.visibility = View.INVISIBLE
    }

    override fun goBack()
    {
        val intent =  Intent(this, AdministratorMain::class.java)
        startActivity(intent)
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
                volounteer.pointId.toString()
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
                    it.pointId.toString()
                )

            )
        }
        return data
    }


    override fun onDataClicked(rowIndex: Int, clickedData: Array<String>?) {

        class MyCustomDialog: DialogFragment(), AdapterView.OnItemSelectedListener {
            var newPoint = 0;

            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {


                dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corners);
                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val rootView : View = inflater.inflate(R.layout.dialog_change_point, container, false)

                var tmp = clickedData?.get(3)
                if (tmp == "null") tmp = "brak"

                val name = rootView.findViewById<TextView>(R.id.imie)
                val lastname = rootView.findViewById<TextView>(R.id.nazwisko)
                val point = rootView.findViewById<TextView>(R.id.point)


                val nameSpan = SpannableString(name?.text.toString() + (clickedData?.get(1)))
                val lastnameSpan = SpannableString(lastname?.text.toString() + (clickedData?.get(2)))
                val pointSpan = SpannableString(point?.text.toString() + tmp)

                nameSpan.setSpan(ForegroundColorSpan(Color.BLUE), name.text.length, nameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                lastnameSpan.setSpan(ForegroundColorSpan(Color.BLUE),lastname.text.length, lastnameSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                pointSpan.setSpan(ForegroundColorSpan(Color.BLUE), point.text.length, pointSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                name.text = nameSpan
                lastname.text = lastnameSpan
                point.text = pointSpan

                rootView.backb.setOnClickListener { dismiss() }

                val myspinner= rootView.findViewById<Spinner>(R.id.spinner)
                val aa = ArrayAdapter(this@AssignVolunteerToPoint, android.R.layout.simple_spinner_item, points)
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
                newPoint = points[0].toInt()

                rootView.edit_button.setOnClickListener {
                    val editedVolounteer = volunteersList.find { volunteer ->
                        volunteer.accountId == clickedData!![0].toInt()
                    }

                    //tu potzrebny trigger
                    editedVolounteer!!.pointId = newPoint
                    dataViewModel.updateVolunteer(editedVolounteer)
                    binding.customSpinner.text = ""
                    dismiss()
                }
                return rootView
            }

            override fun onStart() {
                super.onStart()

                dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val defaultDisplay = DisplayManagerCompat.getInstance(requireContext()).getDisplay(
                    Display.DEFAULT_DISPLAY)
                val displayContext = requireContext().createDisplayContext(defaultDisplay!!)
                val width = displayContext.resources.displayMetrics.widthPixels
                val height = displayContext.resources.displayMetrics.heightPixels

                dialog?.window?.setLayout((width * 0.99).toInt(), (height * 0.55).toInt())
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                newPoint = points[p2].toInt()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        val dialog  = MyCustomDialog()
        dialog.show(supportFragmentManager, "CustomDialog")
    }
}