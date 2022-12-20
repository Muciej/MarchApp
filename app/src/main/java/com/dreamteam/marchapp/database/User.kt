package com.dreamteam.marchapp.database

class User{
    var name: String
    var pass: String

    constructor(_name: String, _pass: String){
        this.name = _name
        this.pass = _pass
    }

    constructor(evName: String){
        when (evName){
            "viewer" -> {
                this.name = "march_viewer"
                this.pass = "V*Yy\$5Zgpm\$fj\$@s"
            }
            else -> {
                this.name = "admin_$evName"
                this.pass = "admin_$evName"
            }

        }
    }
}