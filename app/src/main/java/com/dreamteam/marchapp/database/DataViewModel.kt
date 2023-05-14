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
    private val dbObject: MockDatabase

    init {
        dbObject = MockDatabase.getDatabase(application)

        loggedAcount = dbObject.loggedAcount
        allAccounts = dbObject.allAccounts
        allVolounteers = dbObject.allVolounteers
        allAdmins = dbObject.allAdmins
        allParticipants = dbObject.allParticipants
        checkPoints = dbObject.checkPoints
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
}