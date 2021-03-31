package com.juanarton.consumerapp.`interface`

import com.juanarton.consumerapp.model.DetailDataParcel

interface MainView {
    fun showRecyclerList()
    fun showDetail(dataDetail: DetailDataParcel)
    fun showMessage(messsage : String)
}