package com.juanarton.consumerapp

import android.content.Context
import com.juanarton.consumerapp.model.DataParcel
import com.juanarton.consumerapp.model.DataProcessing
import com.juanarton.consumerapp.model.DetailDataParcel

class MViewModel(private val dataProcessing: DataProcessing) {
    fun getDetail(data: DataParcel): DetailDataParcel {
        return dataProcessing.getDetail(data)
    }

    fun getSocNetwork(username: String, mode: String): ArrayList<DataParcel> {
        return dataProcessing.getSocNetwork(username, mode)
    }

    fun readAllDB(context: Context): ArrayList<DataParcel>{
        return dataProcessing.readAllDB(context)
    }
}