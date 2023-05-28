package com.dreamteam.marchapp.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dreamteam.marchapp.database.dataclasses.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : AndroidViewModel(application){
    val loggedAcount: MutableLiveData<Account?>
    val allAccounts: MutableLiveData<ArrayList<Account>>
    val allVolounteers: MutableLiveData<ArrayList<Volounteer>>
    val allAdmins: MutableLiveData<ArrayList<Administrator>>
    val allParticipants: MutableLiveData<ArrayList<Participant>>
    val checkPoints: MutableLiveData<ArrayList<CheckPoint>>
    val eventsList: MutableLiveData<ArrayList<Event>>
    private val dbObject: MockDatabase

    init {
        dbObject = MockDatabase.getDatabase(application)

        loggedAcount = dbObject.loggedAcount
        allAccounts = dbObject.allAccounts
        allVolounteers = dbObject.allVolounteers
        allAdmins = dbObject.allAdmins
        allParticipants = dbObject.allParticipants
        checkPoints = dbObject.checkPoints
        eventsList = dbObject.eventsList
    }

    fun addNewAccount(account: Account){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.addNewAccount(account)
        }
    }

    fun addNewParticipant(participant: Participant){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.addNewParticipant(participant)
        }
    }

    fun updateParticipant(participant: Participant){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.updateParticipant(participant)
        }
    }

    fun updatePoint(editedPoint: CheckPoint) {
        viewModelScope.launch(Dispatchers.IO){
            dbObject.updatePoint(editedPoint)
        }
    }

    fun deletePoint(pointToDelete: Int) {
        viewModelScope.launch(Dispatchers.IO){
            dbObject.deletePoint(pointToDelete)
        }
    }

    fun addNewVolounteer(volounteer: Volounteer){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.addNewVolounteer(volounteer)
        }
    }

    fun addNewAdmin(administrator: Administrator){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.addNewAdmin(administrator)
        }
    }

    fun addNewCheckPoint(checkPoint: CheckPoint){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.addNewCheckPoint(checkPoint)
        }
    }

    fun loginUser(login: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            dbObject.loginUser(login, password)
        }
    }

    fun chooseEvent(event: Event?){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.chooseEvent(event)
        }
    }

    fun logoutUser(){
        viewModelScope.launch(Dispatchers.IO){
            dbObject.logout()
        }
    }

    fun updateVolunteer(editedVolunteer: Volounteer) {
        viewModelScope.launch(Dispatchers.IO){
            dbObject.updateVolunteer(editedVolunteer)
        }
    }


}