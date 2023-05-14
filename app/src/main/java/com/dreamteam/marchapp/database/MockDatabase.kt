package com.dreamteam.marchapp.database

import android.content.Context
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
    val allAccounts: MutableLiveData<ArrayList<Account>> = MutableLiveData()
    val allVolounteers: MutableLiveData<ArrayList<Volounteer>> = MutableLiveData()
    val allAdmins: MutableLiveData<ArrayList<Administrator>> = MutableLiveData()
    val allParticipants: MutableLiveData<ArrayList<Participant>> = MutableLiveData()
    val checkPoints: MutableLiveData<ArrayList<CheckPoint>> = MutableLiveData()

    init {
        loggedAcount.postValue(null)
        allAccounts.postValue(ArrayList())
        allVolounteers.postValue(ArrayList())
        allAdmins.postValue(ArrayList())
        checkPoints.postValue(ArrayList())
        allParticipants.postValue(ArrayList())
        fillWithSampleData()
    }

    private fun fillWithSampleData() {
        //Adding fake accounts
        allAccounts.value?.add(Account(1, "Organizator", Roles.ORGANISER))
        allAccounts.value?.add(Account(2 , "Administrator", Roles.ADMIN))
        allAccounts.value?.add(Account(3 , "Wolontariusz", Roles.VOLOUNTEER))
        allAccounts.value?.add(Account(4 , "Uczestnik", Roles.PARTICIPANT))
        allAccounts.value?.add(Account(5 , "Muciej", Roles.ADMIN))
        allAccounts.value?.add(Account(8 , "Bartek", Roles.UNKNOWN))
        allAccounts.value?.add(Account(10 , "Wiktoria", Roles.PARTICIPANT))
        allAccounts.value?.add(Account(11, "Tomekkkk", Roles.VOLOUNTEER))
        allAccounts.postValue(allAccounts.value)

        //Adding face Volounteers
        var v = Volounteer(3, 1, "Wolon", "Wolontariuszowy", "629592394", "asdf@sldkf.pl", "Wolontariusz")
        allVolounteers.value?.add(v)
        v = Volounteer(11, 3, "Tomasz", "Hołubczański", "654321666", "tome@asf.pl", "Tomekkkk")
        v.pointId = 1
        allVolounteers.value?.add(v)
        allVolounteers.postValue(allVolounteers.value)

        //Adding fake checkpoints
        checkPoints.value?.add(CheckPoint(1, true, "Start", 0, "20N, 50W"))
        checkPoints.value?.add(CheckPoint(2, false, "Przełęcz rozpaczy", 10, "21N, 51W"))
        checkPoints.value?.add(CheckPoint(3, true, "Skarpa przerażenia", 25, "19.5N, 52W"))
        checkPoints.value?.add(CheckPoint(4, true, "Dyplom", 40, "19.32N, 53W"))
        checkPoints.postValue(checkPoints.value)

        //Adding fake Admins
        allAdmins.value?.add(Administrator(2, 2, "Admin", "Administratorski", "675849206", "asdf@lwekf.pl", "Administrator"))
        allAdmins.value?.add(Administrator(5, 4, "Maciek", "Józefkwef", "666456555", "mauce@joz.com", "Muciej"))
        allAdmins.postValue(allAdmins.value)

        //Adding fake participants
        allParticipants.value?.add(Participant(4, 100, "Uczes", "Uczestnicki", "Ucznen", "wef342"))
        allParticipants.value?.add(Participant(10, 101, "Wiktoria", "Paź", "WikPaź", "we23f2"))
        allParticipants.postValue(allParticipants.value)
    }

    suspend fun addNewParticipant(participant: Participant){
        //todo
    }

    suspend fun addNewAccount(account: Account){
        //todo

    }

    suspend fun addNewVolounteer(volounteer: Volounteer){
        //todo

    }

    suspend fun addNewAdmin(administrator: Administrator){
        //todo

    }

    suspend fun addNewCheckPoint(checkPoint: CheckPoint){
        //todo

    }

}