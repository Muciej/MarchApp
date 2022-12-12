package com.dreamteam.marchapp.logic.shared

object CodeQr {
    var STRING_LENGTH = 10

    fun randomString() : String{
        val alphabet = "abcdefghijklmnopqrstuvwxyzABCD@$#*123456789"
        var randomStr = ""
        for(i in 1..STRING_LENGTH)
        {
            randomStr += alphabet.random()
        }
        return randomStr;
    }

    fun validateString(str : String) : Boolean {
        //TODO zapytanie do bazy czy istnieje taki kod (narazie zhardcodowane)
        var stringsFromDb = arrayListOf("abcdefghij", "abplsadhij", "abAIFSJDNIij")
        for(s in stringsFromDb)
        {
            if(str.equals(s))
            {
                return true
            }
        }
        return false
    }


    fun createCode() : String {
        var code = randomString();

        if(validateString(code)){
            code = randomString();
        }

        return code;
    }
}