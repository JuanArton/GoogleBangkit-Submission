package com.juanarton.githubapp.`interface`

import com.juanarton.githubapp.model.DetailDataParcel

interface MainView {
    fun showRecyclerList()
    fun showDetail(dataDetail: DetailDataParcel)
    fun showMessage(messsage : String)
}