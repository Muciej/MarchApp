package com.dreamteam.marchapp.database

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import java.sql.*
import java.util.*


class JDBCConnector() : DBConnector {

    private var databaseName = ""
//    private val dbUrl: String = "jdbc:jtds:mysql://localhost:3306/"
    private var port: String = "3306"
    private var ip: String = "10.182.205.135"
//    private var ip: String = "localhost"
    private var dbConnection: Connection? = null
    private var currQuery: PreparedStatement? = null
    private var currentRes: ResultSet? = null
    private var varNo: Int = 0

//    override fun setDBName(name: String) {
//        this.databaseName = name
//        prepareQuery("use $databaseName;")
//        executeQuery()
//        println("DB name set to $name")
//    }

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
            throw Exception("Last query did not return any result!")

        val metadata = currentRes!!.metaData
        val colNo = metadata.columnCount
        val answer = Vector<Vector<String>>()
        answer.add(getCurrRow(colNo))
        while(currentRes!!.next()){
            answer.add(getCurrRow(colNo))
        }
        currentRes!!.close()
        currentRes = null
        return answer
    }

    fun getCurrRow(colNo: Int): Vector<String>{
        val row = Vector<String>()
        for (i: Int in 1..colNo){
            row.add(currentRes?.getString(i))
        }
        return row

    }

    override fun getRow(rowNo: Int): Vector<String> {
        for(i in 1 until rowNo){
            currentRes?.next()
        }
        val answer = getCurrRow(currentRes!!.metaData.columnCount)
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun getCol(colName: String): Vector<String> {
        val answer = Vector<String>()
        answer.add(currentRes!!.getString(colName))
        while(currentRes?.next() == true){
            answer.add(currentRes!!.getString(colName))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun getCol(colNo: Int): Vector<String> {
        val answer = Vector<String>()
        answer.add(currentRes?.getString(colNo))
        while(currentRes?.next() == true){
            answer.add(currentRes!!.getString(colNo))
        }
        currentRes?.close()
        currentRes = null
        return answer
    }

    override fun startConnection(user: User, dbName: String) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Class.forName("com.mysql.jdbc.Driver").newInstance()
        closeConnection()
        try {
            val connUrl = "jdbc:mysql://$ip:$port/$dbName"
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
    }

    override fun executeQuery() {
        if(currQuery == null)
            return
        try {
            if(currQuery!!.execute())
                currentRes = currQuery!!.resultSet
            currQuery?.close()
            currQuery = null
        } catch (e: SQLException){
            e.printStackTrace()
            println("Could not execute query")
        }
    }
}