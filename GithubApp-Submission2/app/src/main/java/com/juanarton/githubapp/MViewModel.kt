package com.juanarton.githubapp

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
}