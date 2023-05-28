package com.dreamteam.marchapp.logic.config

import java.security.MessageDigest

class PasswordEncoder {
    companion object {
        fun hash(pass: String): String {
            val bytes = pass.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}