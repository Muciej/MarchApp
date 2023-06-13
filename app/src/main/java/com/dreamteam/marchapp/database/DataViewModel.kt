package com.dreamteam.marchapp.database

import android.app.Application
import android.util.Log
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

    fun existsCheckPointByName(name: String): Boolean? {
        return dbObject.checkPoints.value?.any{ checkPoint -> checkPoint.name == name}
    }

    fun existsCheckPointByKm(km: Int): Boolean? {
        return dbObject.checkPoints.value?.any{ checkPoint -> checkPoint.dist == km}
    }

    fun existsCheckPointByCords(cords: String): Boolean? {
        return dbObject.checkPoints.value?.any{ checkPoint -> checkPoint.coords == cords}
    }

    fun loginUser(login: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            dbObject.loginUser(login, password)
        }
    }

    fun changeUserPassword(id: Int, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbObject.changeUserPassword(id, newPassword)
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

    fun getVolounteer(account: Account?) : Volounteer?{
        if(account == null || allVolounteers.value == null){
            Log.i("DataViewModel", "Null volounteer list or account")
            return null
        }
        for (volounteer in allVolounteers.value!!){
            if( volounteer.accountId == account.id){
                return volounteer
            }

        }
        return null
    }

    fun getParticipantByQR(qr: String?) : Participant?{
        if(qr == null || allParticipants.value == null){
            Log.i("DataViewModel", "Null participant list or qr")
            return null
        }
        for (participant in allParticipants.value!!){
            if(participant.qrCodeData == qr){
                return participant
            }

        }
        return null
    }

    fun markPointReached(pointId: Int?, participant: Participant?, currentDate: String) {
        viewModelScope.launch(Dispatchers.IO){
            dbObject.markPointReached(pointId, participant, currentDate);
        }
    }
}