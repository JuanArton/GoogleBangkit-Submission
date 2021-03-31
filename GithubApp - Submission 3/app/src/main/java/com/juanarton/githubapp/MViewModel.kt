package com.juanarton.githubapp

import android.content.Context
import com.juanarton.githubapp.model.DataParcel
import com.juanarton.githubapp.model.DataProcessing
import com.juanarton.githubapp.model.DetailDataParcel

class MViewModel(private val dataProcessing: DataProcessing) {
    fun getData(username: String): ArrayList<DataParcel> {
        return dataProcessing.getData(username)
    }

    fun getDetail(data: DataParcel): DetailDataParcel{
        return dataProcessing.getDetail(data)
    }

    fun getSocNetwork(username: String, mode: String): ArrayList<DataParcel> {
        return dataProcessing.getSocNetwork(username, mode)
    }

    fun addToDB(context: Context, username: String, id: String){
        dataProcessing.addToDB(context, username, id)
    }

    fun readAllDB(context: Context): ArrayList<DataParcel>{
        return dataProcessing.readAllDB(context)
    }

    fun readDB(context: Context): ArrayList<DataParcel>{
        return dataProcessing.readDB(context)
    }

    fun deleteFromDB(context: Context, id: String){
        dataProcessing.deleteFromDb(context, id)
    }
}