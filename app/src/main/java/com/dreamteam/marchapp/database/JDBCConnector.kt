package com.dreamteam.marchapp.database

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.sql.*
import java.util.*


object JDBCConnector : DBConnector {

    private var port: String = "3306"
    private var ip: String = "marchapp.sytes.net"
//    private var ip: String = "192.168.8.123"
    private var dbConnection: Connection? = null
    private var currQuery: PreparedStatement? = null
    private var currentRes: ResultSet? = null
    private var dbName = "viewer"
    private var flags = "?autoReconnect=true&verifyServerCertificate=false&useSSL=true"

    override fun setDBName(name: String) {
        dbName = name
        println("DB name set to $name")
    }

    override fun prepareQuery(query: String, varNo: Int) {
        currQuery?.close()
        try {
            currQuery = dbConnection?.prepareStatement(query)
        } catch (e : SQLException){
            e.printStackTrace()
        }
    }

    override fun setStrVar(v: String, varNo: Int) {
        currQuery?.setString(varNo, v)
    }

    override fun setIntVar(v: Int, varNo: Int) {
        currQuery?.setInt(varNo, v)
    }

    override fun getAnswer(): Vector<Vector<String>> {
        if (currentRes == null)
            throw Exception("No result!")

        val metadata = currentRes!!.metaData
        val colNo = metadata.columnCount
        val answer = Vector<Vector<String>>()
        answer.add(getCurrRow(colNo))
        while(currentRes!!.next()){
            answer.add(getCurrRow(colNo))
        }
        return answer
    }

    override fun getCurrRow(colNo: Int): Vector<String>{
        if (currentRes == null)
            throw Exception("No result!")
        val row = Vector<String>()
        for (i: Int in 1..colNo){
            row.add(currentRes?.getString(i))
        }
        return row
    }

    override fun getRow(rowNo: Int, colNo: Int): Vector<String> {
        if (currentRes == null)
            throw Exception("No result!")
        val row = Vector<String>()
        for(i in 1 until rowNo) {
            currentRes?.next()
        }
        for (i: Int in 1..colNo){
            row.add(currentRes?.getString(i))
        }
        return row
    }

    override fun getCol(colName: String): Vector<String> {
        if (currentRes == null)
            throw Exception("No result!")
        val answer = Vector<String>()
        answer.add(currentRes?.getString(colName))
        while(currentRes?.next() == true){
            answer.add(currentRes?.getString(colName))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun getCol(colNo: Int): Vector<String> {
        if (currentRes == null)
            throw Exception("No result!")
        val answer = Vector<String>()
        answer.add(currentRes?.getString(colNo))
        while(currentRes?.next() == true){
            println(currentRes?.getString(colNo))
            answer.add(currentRes?.getString(colNo))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun getColInts(colNo: Int): Vector<Int> {
        if (currentRes == null)
            throw Exception("No result!")
        val answer = Vector<Int>()

        answer.add(currentRes?.getInt(colNo))
        while(currentRes?.next() == true){
            println(currentRes?.getString(colNo))
            answer.add(currentRes?.getInt(colNo))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun getColBools(colNo: Int): Vector<Boolean> {
        if (currentRes == null)
            throw Exception("No result!")
        val answer = Vector<Boolean>()

        answer.add(currentRes?.getBoolean(colNo))
        while(currentRes?.next() == true){
            println(currentRes?.getString(colNo))
            answer.add(currentRes?.getBoolean(colNo))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun startConnection() {
        if(dbConnection != null)
            return
        val user = User(dbName)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        closeConnection()
        try {
            val connUrl : String
            if(dbName == "viewer"){
                connUrl = "jdbc:mysql://$ip:$port/baza_biegow_przelajowych$flags"
            } else {
                connUrl = "jdbc:mysql://$ip:$port/$dbName$flags"
            }
            val props = Properties()
            props["user"] = user.name
            props["password"] = user.pass
            dbConnection = DriverManager.getConnection(connUrl, user.name, user.pass)
            println("Connection established as " + user.name)
        } catch (e: SQLException){
            e.printStackTrace()
            println("Couldn't establish connection")
        }
    }

    override fun closeConnection() {
        if(dbConnection != null)
            dbConnection?.close()
        dbConnection = null
        currQuery = null
        currentRes = null
        println("Connection to db closed!")
    }

    override fun executeQuery() {
        try {
            println("Executing query")
            if(currQuery?.execute() == true){
                currentRes = currQuery?.resultSet
            }
        } catch (e: SQLException){
            e.printStackTrace()
            println("Could not execute query")
        }
        if(currentRes == null || !currentRes!!.next()){
            currentRes = null;
            println("Query with no or empty result");
        }
    }

    override fun closeQuery() {
        currQuery?.close()
        currQuery = null
    }

}