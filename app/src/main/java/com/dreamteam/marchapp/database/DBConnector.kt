package com.dreamteam.marchapp.database

import java.util.Vector

interface DBConnector {
    fun prepareQuery(query: String, varNo: Int = 0)
    fun setVar(v: String, varNo: Int)
    fun getAnswer(): Vector<Vector<String>>
    fun getRow(rowNo: Int)
    fun getCol(colName: String)
    fun getCol(colNo: Int)
    fun startConnection(user: User)
    fun closeConnection()
}