package com.dreamteam.marchapp.database

import java.util.Vector

interface DBConnector {
    fun setDBName(name: String)
    fun prepareQuery(query: String, varNo: Int = 0)
    fun setStrVar(v: String, varNo: Int)
    fun setIntVar(v: Int, varNo: Int)
    fun getAnswer(): Vector<Vector<String>>
    fun getRow(rowNo: Int) : Vector<String>
    fun getCol(colName: String): Vector<String>
    fun getCol(colNo: Int): Vector<String>
    fun startConnection(user: User)
    fun closeConnection()
    fun executeQuery()
}