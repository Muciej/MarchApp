package com.dreamteam.marchapp.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dreamteam.marchapp.database.dataclasses.*

class MockDatabase {
    companion object{
        @Volatile
        private var INSTANCE: MockDatabase? = null

        fun getDatabase(context: Context): MockDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = MockDatabase()
                INSTANCE = instance
                return instance
            }
        }
    }

    val loggedAcount: MutableLiveData<Account?> = MutableLiveData()
    val loggedEvent: MutableLiveData<Event?> = MutableLiveData()
    val eventsList: MutableLiveData<ArrayList<Event>> = MutableLiveData()
    val allAccounts: MutableLiveData<ArrayList<Account>> = MutableLiveData()
    val allVolounteers: MutableLiveData<ArrayList<Volounteer>> = MutableLiveData()
    val allAdmins: MutableLiveData<ArrayList<Administrator>> = MutableLiveData()
    val allParticipants: MutableLiveData<ArrayList<Participant>> = MutableLiveData()
    val checkPoints: MutableLiveData<ArrayList<CheckPoint>> = MutableLiveData()

    init {
        loggedAcount.postValue(null)
        loggedEvent.postValue(null)
        fillWithSampleData()
    }

    private fun fillWithSampleData() {
        //Adding fake events
        val tempEv = ArrayList<Event>()
        tempEv.add(Event(1,"Beskida", "ev_beskida", 1))
        tempEv.add(Event(2, "JFTTRun", "ev_jfttrun", 1))
        eventsList.postValue(tempEv)

        //Adding fake accounts
        val tempAcc = ArrayList<Account>()
        tempAcc.add(Account(1, "Organizator", Roles.ORGANISER))
        tempAcc.add(Account(2 , "Administrator", Roles.ADMIN))
        tempAcc.add(Account(3 , "Wolontariusz", Roles.VOLOUNTEER))
        tempAcc.add(Account(4 , "Uczestnik", Roles.PARTICIPANT))
        tempAcc.add(Account(5 , "Muciej", Roles.ADMIN))
        tempAcc.add(Account(8 , "Bartek", Roles.UNKNOWN))
        tempAcc.add(Account(10 , "Wiktoria", Roles.PARTICIPANT))
        tempAcc.add(Account(11, "Tomekkkk", Roles.VOLOUNTEER))
        allAccounts.postValue(tempAcc)

        //Adding face Volounteers
        val tempVol = ArrayList<Volounteer>()
        var v = Volounteer(3, 1, "Wolon", "Wolontariuszowy", "629592394", "asdf@sldkf.pl", "Wolontariusz")
        tempVol.add(v)
        v = Volounteer(11, 3, "Tomasz", "Hołubczański", "654321666", "tome@asf.pl", "Tomekkkk")
        v.pointId = 1
        tempVol.add(v)
        allVolounteers.postValue(tempVol)

        //Adding fake checkpoints
        val tempCheck = ArrayList<CheckPoint>()
        tempCheck.add(CheckPoint(1, true, "Start", 0, "20N, 50W"))
        tempCheck.add(CheckPoint(2, false, "Przełęcz rozpaczy", 10, "21N, 51W"))
        tempCheck.add(CheckPoint(3, true, "Skarpa przerażenia", 25, "19.5N, 52W"))
        tempCheck.add(CheckPoint(4, true, "Dyplom", 40, "19.32N, 53W"))
        checkPoints.postValue(tempCheck)

        //Adding fake Admins
        val tempAdmins = ArrayList<Administrator>()
        tempAdmins.add(Administrator(2, 2, "Admin", "Administratorski", "675849206", "asdf@lwekf.pl", "Administrator"))
        tempAdmins.add(Administrator(5, 4, "Maciek", "Józefkwef", "666456555", "mauce@joz.com", "Muciej"))
        allAdmins.postValue(tempAdmins)

        //Adding fake participants
        val tempPart = ArrayList<Participant>()
        tempPart.add(Participant(4, 100, "Uczes", "Uczestnicki", "Ucznen", "wef342"))
        tempPart.add(Participant(10, 101, "Wiktoria", "Paź", "WikPaź", "we23f2"))
        allParticipants.postValue(tempPart)
    }

    suspend fun addNewParticipant(participant: Participant){
        allParticipants.value?.add(participant)
        allParticipants.postValue(allParticipants.value)
    }

    fun updateParticipant(participant: Participant) {
        val indexToUpdate = allParticipants.value?.indexOfFirst { existingParticipant ->
            existingParticipant.accId == participant.accId}!!

        allParticipants.value?.set(indexToUpdate, participant)
        allParticipants.postValue(allParticipants.value)
    }

    suspend fun addNewAccount(account: Account){
        allAccounts.value?.add(account)
        allAccounts.postValue(allAccounts.value)
    }

    fun updateVolunteer(editedVolunteer: Volounteer) {
        val indexToUpdate = allVolounteers.value?.indexOfFirst { existingVolunteer->
            existingVolunteer.accountId == editedVolunteer.accountId}!!

        allVolounteers.value?.set(indexToUpdate, editedVolunteer)
        allVolounteers.postValue(allVolounteers.value)
    }

    suspend fun addNewVolounteer(volounteer: Volounteer){
        allVolounteers.value?.add(volounteer)
        allVolounteers.postValue(allVolounteers.value)
    }

    suspend fun addNewAdmin(administrator: Administrator){
        allAdmins.value?.add(administrator)
        allAdmins.postValue(allAdmins.value)
    }

    suspend fun addNewCheckPoint(checkPoint: CheckPoint){
        checkPoints.value?.add(checkPoint)
        checkPoints.postValue(checkPoints.value)
    }

    fun loginUser(login: String, password: String) {
        var found = false
        for (u in allAccounts.value!!){
            if(u.login == login) {
                loggedAcount.postValue(u)
                found = true
                break
            }
        }
        if(!found){
            loggedAcount.postValue(null)
        }
    }

    fun chooseEvent(event: Event?){
        loggedEvent.postValue(event)
    }

    fun logout() {
        loggedAcount.postValue(null)
    }




}