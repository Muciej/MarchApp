package com.dreamteam.marchapp.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.dreamteam.marchapp.database.dataclasses.Account
import com.dreamteam.marchapp.database.dataclasses.Administrator
import com.dreamteam.marchapp.database.dataclasses.CheckPoint
import com.dreamteam.marchapp.database.dataclasses.Event
import com.dreamteam.marchapp.database.dataclasses.Participant
import com.dreamteam.marchapp.database.dataclasses.Volounteer

@SuppressLint("AuthLeak")
class AzureDatabase {
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

    val connStrings: Map<String, String>
    val loggedAcount: MutableLiveData<Account?> = MutableLiveData()
    val loggedEvent: MutableLiveData<Event?> = MutableLiveData()
    val eventsList: MutableLiveData<ArrayList<Event>> = MutableLiveData()
    val allAccounts: MutableLiveData<ArrayList<Account>> = MutableLiveData()
    val allVolounteers: MutableLiveData<ArrayList<Volounteer>> = MutableLiveData()
    val allAdmins: MutableLiveData<ArrayList<Administrator>> = MutableLiveData()
    val allParticipants: MutableLiveData<ArrayList<Participant>> = MutableLiveData()
    val checkPoints: MutableLiveData<ArrayList<CheckPoint>> = MutableLiveData()

    init{
        connStrings = HashMap()
        connStrings["ev_beskida"] = "jdbc:sqlserver://marchappserver.database.windows.net:1433;database=ev_beskida;user=web_root@marchappserver;password={your_password_here};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
        connStrings["ev_"]
    }

    suspend fun addNewParticipant(participant: Participant){

    }

    suspend fun addNewAccount(account: Account){

    }

    suspend fun addNewVolounteer(volounteer: Volounteer){

    }

    suspend fun addNewAdmin(administrator: Administrator){

    }

    suspend fun addNewCheckPoint(checkPoint: CheckPoint){

    }

    fun loginUser(login: String, password: String) {

    }

    fun chooseEvent(event: Event?){

    }

    fun logout() {
        loggedAcount.postValue(null)
    }


}