package com.dreamteam.marchapp.database

import java.util.Vector

interface DBConnector {
    fun setDBName(name: String)
    fun setCurrentUserID(id : Int)
    fun getCurrentUserID() : Int
    fun prepareQuery(query: String, varNo: Int = 0)
    fun setStrVar(v: String, varNo: Int)
    fun setIntVar(v: Int, varNo: Int)
    fun getAnswer(): Vector<Vector<String>>
    fun getRow(rowNo: Int, colNo: Int) : Vector<String>
    fun getCurrRow(colNo: Int) : Vector<String>
    fun getCol(colName: String): Vector<String>
    fun getCol(colNo: Int): Vector<String>
    fun getColInts(colNo: Int): Vector<Int>
    fun getColBools(colNo: Int): Vector<Boolean>
    fun startConnection()
    fun closeConnection()
    fun executeQuery()
    fun closeQuery()
}