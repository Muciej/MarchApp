package com.dreamteam.marchapp.database

class User{
    var name: String
    var pass: String

    constructor(_name: String, _pass: String){
        this.name = _name
        this.pass = _pass
    }

    constructor(_defUsr: String){
        when (_defUsr){
            "register" -> {
                this.name = "register"
                this.pass = "register"
            }
            "viewer" -> {
                this.name = "march_viewer"
                this.pass = "V*Yy\$5Zgpm\$fj\$@s"
            }
            else -> {
                this.name = "login"
                this.pass = "login"
            }

        }
    }
}