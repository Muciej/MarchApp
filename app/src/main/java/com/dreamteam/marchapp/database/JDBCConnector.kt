package com.dreamteam.marchapp.database

import java.util.*
import java.sql.*

class JDBCConnector : DBConnector {

    final val dbUrl: String = "jdbc:mysql://localhost:3306/";
    var dbConnection: Connection? = null
    var currQuery: PreparedStatement? = null
    var varNo: Int = 0

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
        TODO("Not yet implemented")
    }

    override fun getRow(rowNo: Int) {
        TODO("Not yet implemented")
    }

    override fun getCol(colName: String) {
        TODO("Not yet implemented")
    }

    override fun getCol(colNo: Int) {
        TODO("Not yet implemented")
    }

    override fun startConnection(user: User) {
        closeConnection()
        dbConnection = DriverManager.getConnection(dbUrl, user.name, user.pass)
    }

    override fun closeConnection() {
        dbConnection?.close()
        dbConnection = null
    }
}