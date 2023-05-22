package com.dreamteam.marchapp.logic.shared

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Display
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dreamteam.marchapp.database.DataViewModel
import com.dreamteam.marchapp.databinding.ActivityOrganiserShowVolunteersBinding
import com.dreamteam.marchapp.logic.admin.AdministratorMain
import com.dreamteam.marchapp.logic.organiser.OrganisatorMain
import com.dreamteam.marchapp.logic.validation.LastNameValidator
import com.dreamteam.marchapp.logic.validation.NameValidator
import com.dreamteam.marchapp.logic.volunteer.VolunteerMain
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.model.TableColumnPxWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.dialog_edit_user.*
import kotlinx.android.synthetic.main.dialog_edit_user.view.*
import kotlinx.android.synthetic.main.dialog_zoom_data.view.*
import java.util.*

abstract class ShowAndEditObject: AppCompatActivity(), TableDataClickListener<Array<String>> {
    var data : MutableList<Array<String>> = mutableListOf()
    lateinit var tableView : TableView<Array<String>>
    private lateinit var adapter : ArrayAdapter<String>
    var peopleNames = Vector<String>()
    lateinit var dataViewModel: DataViewModel
    lateinit var binding: ActivityOrganiserShowVolunteersBinding
    private lateinit var dataAdapter : SimpleTableDataAdapter
    private lateinit var peopleToShow : MutableList<Array<String>>
    var accessLevel = ""

    abstract fun setAccesssLevel()
    abstract fun setHeader()
    abstract fun selectData(name: String):MutableList<Array<String>>
    abstract fun initData():MutableList<Array<String>>
    abstract fun checkIfPresentInDB(obj : String, field : String, id:Int) : Boolean


    open fun setDataViewModel() {
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
    }



    fun init_spinner_and_table() {
        adapter = ArrayAdapter(this@ShowAndEditObject, R.layout.simple_list_item_1, peopleNames)
        peopleToShow = initData()

        dataAdapter.data.clear()
        dataAdapter.addAll(peopleToShow)
        dataAdapter.notifyDataSetChanged()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganiserShowVolunteersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDataViewModel()
        setTable()
        setAccesssLevel()

        binding.btnBack.setOnClickListener{ goBack() }
        binding.customSpinner.setOnClickListener{ show_dialog() }
    }


    open fun goBack()
    {
        lateinit var intent : Intent
        when (accessLevel) {
            "Organiser" -> intent = Intent(this, OrganisatorMain::class.java)
            "Admin" -> intent = Intent(this, AdministratorMain::class.java)
            "Volunteer" -> intent = Intent(this, VolunteerMain::class.java)
        }
        startActivity(intent)
    }



    private fun setTable() {
        tableView = findViewById<View>(com.dreamteam.marchapp.R.id.volunteersTable) as TableView<Array<String>>
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

        setHeader()

        val columnModel = TableColumnPxWidthModel(5, 200)
        columnModel.setColumnWidth(0, 80)
        columnModel.setColumnWidth(1, 200)
        columnModel.setColumnWidth(2, 250)
        columnModel.setColumnWidth(3, 250)
        columnModel.setColumnWidth(4, 350)
        tableView.columnModel = columnModel

        peopleToShow = arrayListOf()
        dataAdapter = SimpleTableDataAdapter(this@ShowAndEditObject, peopleToShow)

        dataAdapter.setTextSize(12)
        dataAdapter.setTextColor(Color.BLACK)
        tableView.dataAdapter = dataAdapter
    }



    private fun show_dialog() {
        val dialog = Dialog(this@ShowAndEditObject)
        dialog.setContentView(com.dreamteam.marchapp.R.layout.dialog_searchable_spinner)
        dialog.show()

        val edittext = dialog.findViewById<EditText>(com.dreamteam.marchapp.R.id.edit_text)
        val listview = dialog.findViewById<ListView>(com.dreamteam.marchapp.R.id.listView)

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

                if (listview.getItemAtPosition(clicked).toString().lowercase() == "wszyscy") peopleToShow = initData()
                else if (listview.getItemAtPosition(clicked).toString().lowercase()!="nie wybrano") peopleToShow = selectData(listview.getItemAtPosition(clicked).toString())
                else peopleToShow.clear()

                dataAdapter.data.clear()
                dataAdapter.addAll(peopleToShow)
                dataAdapter.notifyDataSetChanged()

                dialog.dismiss()
            }
    }



    open class BaseDialogFragment : DialogFragment() {

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

        protected fun showInfo(message: String) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        open fun validateEditedData(vararg values: String): Boolean {
            for (value in values) {
                if (value.isBlank()) {
                    Toast.makeText(requireContext(), "Żadne z pól nie może być puste", Toast.LENGTH_SHORT).show()
                    return false
                }
            }

            if (!NameValidator.validate(values[0])) {
                showInfo("Nieprawidłowy format imienia!")
                return false
            }

            if (!LastNameValidator.validate(values[1])) {
                showInfo("Nieprawidłowy format nazwiska!")
                return false
            }

            return true
        }
    }












}




